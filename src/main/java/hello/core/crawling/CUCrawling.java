package hello.core.crawling;

import hello.core.crawling.dto.CrawlingDTO;
import hello.core.crawling.entity.CrawlingEntity;
import hello.core.crawling.service.CrawlingService;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.JavascriptExecutor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CUCrawling {
    private final CrawlingService crawlingService; // 🔥 CrawlingService 주입

    public void startCrawling() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get("https://cu.bgfretail.com/product/product.do?category=product&depth2=4&depth3=1");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            for (int depth = 1; depth <= 7; depth++) {
                String depthXPath = "//*[@id='contents']/div[1]/ul/li[" + depth + "]/a";

                try {
                    WebElement depthButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(depthXPath)));
                    String depthName = depthButton.getText().trim();
                    depthButton.click();
                    System.out.println("\n🔹 카테고리 [" + depthName + "] 클릭! 🔹");
                    Thread.sleep(2000);
                } catch (Exception e) {
                    System.out.println("✅ 더 이상 depth3 버튼이 없습니다. 종료합니다.");
                    break;
                }

                int einfoIndex = 2;
                while (true) {
                    String einfoXPath = "/html/body/form/div[3]/div[3]/div[2]/div[1]/ul/li[" + einfoIndex + "]/a";
                    String category = "";

                    try {
                        WebElement einfoButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(einfoXPath)));
                        category = einfoButton.getText().trim();
                        einfoButton.click();
                        System.out.println("\n🔹 카테고리 [" + category + "] 클릭! 🔹");
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        System.out.println("✅ 더 이상 선택할 einfo 버튼이 없습니다. 종료합니다.");
                        break;
                    }

                    while (true) {
                        try {
                            WebElement loadMoreButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='dataTable']/div[last()]/div[1]/a")));
                            loadMoreButton.click();
                            Thread.sleep(2000);
                        } catch (Exception e) {
                            System.out.println("더보기 버튼을 찾을 수 없거나 모든 콘텐츠를 불러왔습니다.");
                            break;
                        }
                    }

                    List<WebElement> productContainers = driver.findElements(By.xpath("//*[@id='dataTable']/div"));
                    for (WebElement container : productContainers) {
                        List<WebElement> productNames = container.findElements(By.xpath(".//ul/li/div/div[1]/div[2]/div[1]/p"));
                        List<WebElement> productPrices = container.findElements(By.xpath(".//ul/li/div/div[1]/div[2]/div[2]/strong"));
                        List<WebElement> productEvents = container.findElements(By.xpath(".//ul/li/div/div[2]/span"));
                        List<WebElement> productImages = container.findElements(By.xpath(".//ul/li/div/div[1]/div[1]/img"));

                        for (int i = 0; i < productNames.size(); i++) {
                            try {
                                String name = productNames.get(i).getText();
                                String price = productPrices.size() > i ? productPrices.get(i).getText() : "0";
                                // int intPrice = Integer.parseInt(price.replaceAll("[^0-9]", "")); // 필요 시 사용
                                String event = (productEvents.size() > i) ? productEvents.get(i).getAttribute("class") : "행사 없음";
                                String imageUrl = productImages.size() > i ? productImages.get(i).getAttribute("src") : "이미지 없음";

                                if (imageUrl.startsWith("//")) {
                                    imageUrl = "https:" + imageUrl;
                                }

                                // 크롤링한 데이터 DB에 저장
                                CrawlingDTO dto = new CrawlingDTO();
                                dto.setCompanyName("CU");
                                dto.setProductNames(name);
                                dto.setProductPrices(price);
                                dto.setProductImages(imageUrl);
                                dto.setDiscountInfo(event);
                                dto.setProductCategory(category);
                                dto.setBarcode(null);

                                crawlingService.saveOrUpdate(dto); // DTO 저장

                            } catch (Exception e) {
                                System.err.println("❌ 데이터 처리 중 오류 발생: " + e.getMessage());
                                e.printStackTrace(); // 로그 남기기
                            }
                        }
                    }

                    System.out.println("✅ 카테고리 [" + category + "] 크롤링 완료 ✅\n");
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("window.scrollTo(0, 0);");

                    einfoIndex++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}

