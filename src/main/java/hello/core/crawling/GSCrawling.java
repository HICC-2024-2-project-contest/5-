package hello.core.crawling;

import hello.core.crawling.dto.CrawlingDTO;
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
public class GSCrawling {
    private final CrawlingService crawlingService; // 🔥 CrawlingService 주입

    public void startCrawling() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get("http://gs25.gsretail.com/gscvs/en/products/event-goods#;");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // 안전하지 않은 사이트 경고 처리
            if (driver.getTitle().contains("안전하지 않은 사이트")) {
                WebElement HTTPButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@id='proceed-button']")));
                HTTPButton.click();
            }

            // ✅ TOTAL 버튼 클릭
            WebElement totalButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='TOTAL']")));
            totalButton.click();
            Thread.sleep(2000);

            // ✅ 상품 리스트 첫 번째 요소가 로드될 때까지 대기
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='wrap']/div[4]/div[2]/div[3]/div/div/div[4]/ul/li[1]")));

            while (true) {
                // 현재 페이지 번호 가져오기
                WebElement pageNumElement = driver.findElement(By.xpath("//*[@id='pageNum']"));
                String currentPage = pageNumElement.getAttribute("value");

                // 제품 목록 가져오기
                List<WebElement> productContainers = driver.findElements(By.xpath("//*[@id='wrap']/div[4]/div[2]/div[3]/div/div/div[4]/ul/li"));

                for (WebElement container : productContainers) {
                    try {
                        // ✅ 제품명
                        String name = container.findElement(By.xpath("./div/p[2]")).getText();

                        // ✅ 가격
                        String price = container.findElement(By.xpath("./div/p[3]/span")).getText();

                        // ✅ 행사 여부 (없으면 기본값)
                        List<WebElement> eventElements = container.findElements(By.xpath("./div/div/p/span"));
                        String event = eventElements.isEmpty() ? "행사 없음" : eventElements.get(0).getText();

                        // ✅ 이미지 URL
                        WebElement imageElement = container.findElement(By.xpath("./div/p[1]/img"));
                        String imageUrl = imageElement.getAttribute("src");
                        if (imageUrl.startsWith("//")) {
                            imageUrl = "https:" + imageUrl;
                        }

                        String category = "";

                        // 크롤링한 데이터 DB에 저장
                        CrawlingDTO dto = new CrawlingDTO();
                        dto.setCompanyName("GS");
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

                // Next 버튼 찾기
                WebElement nextButton = driver.findElement(By.xpath("//*[@id='wrap']/div[4]/div[2]/div[3]/div/div/div[4]/div/a[3]"));

                if (!nextButton.isDisplayed() || !nextButton.isEnabled()) {
                    break;
                }

                // Next 버튼 클릭
                nextButton.click();
                Thread.sleep(2000);

                // 페이지가 로드될 시간을 기다림
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='wrap']/div[4]/div[2]/div[3]/div/div/div[4]/ul/li[1]")));

                // 새로운 페이지 번호 가져오기
                String newPage = driver.findElement(By.xpath("//*[@id='pageNum']")).getAttribute("value");

                // 페이지가 변하지 않았다면 마지막 페이지이므로 종료
                if (currentPage.equals(newPage)) {
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
