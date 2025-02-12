package hello.core.crawling;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;
import java.util.List;

public class CUCrawling {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");

        // ChromeDriver 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized"); // 브라우저 최대화
        WebDriver driver = new ChromeDriver(options);

        try {
            // ✅ 웹사이트 열기
            driver.get("https://cu.bgfretail.com/product/product.do?category=product&depth2=4&depth3=1");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            for (int depth = 1; depth <= 7; depth++) { // ✅ depth3 버튼 클릭 (2~7)
                String depthXPath = "//*[@id='contents']/div[1]/ul/li[" + depth + "]/a";

                try {
                    WebElement depthButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(depthXPath)));
                    String depthName = depthButton.getText().trim();
                    depthButton.click(); // ✅ depth 버튼 클릭
                    System.out.println("\n🔹 카테고리 [" + depthName + "] 클릭! 🔹");
                    Thread.sleep(2000); // 로딩 대기
                } catch (Exception e) {
                    System.out.println("✅ 더 이상 depth3 버튼이 없습니다. 종료합니다.");
                    break;
                }

                // ✅ einfo 버튼 반복 클릭 (li[2]부터 버튼이 없어질 때까지)
                int einfoIndex = 2;
                while (true) {
                    String einfoXPath = "/html/body/form/div[3]/div[3]/div[2]/div[1]/ul/li[" + einfoIndex + "]/a";
                    String category = "";  // 카테고리 변수

                    try {
                        WebElement einfoButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(einfoXPath)));
                        category = einfoButton.getText().trim(); // ✅ 카테고리 이름 저장
                        einfoButton.click(); // einfo 버튼 클릭
                        System.out.println("\n🔹 카테고리 [" + category + "] 클릭! 🔹");
                        Thread.sleep(2000); // 로딩 대기
                    } catch (Exception e) {
                        System.out.println("✅ 더 이상 선택할 einfo 버튼이 없습니다. 종료합니다.");
                        break;
                    }

                    // ✅ "더보기" 버튼 클릭 반복 (데이터 끝까지 로드)
                    while (true) {
                        try {
                            WebElement loadMoreButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='dataTable']/div[last()]/div[1]/a")));
                            loadMoreButton.click(); // "더보기" 버튼 클릭
                            Thread.sleep(2000); // 로딩 대기
                        } catch (Exception e) {
                            System.out.println("더보기 버튼을 찾을 수 없거나 모든 콘텐츠를 불러왔습니다.");
                            break;
                        }
                    }

                    // ✅ 모든 제품명, 가격, 행사 정보, 이미지 URL 추출
                    List<WebElement> productContainers = driver.findElements(By.xpath("//*[@id='dataTable']/div"));
                    for (WebElement container : productContainers) {
                        List<WebElement> productNames = container.findElements(By.xpath(".//ul/li/div/div[1]/div[2]/div[1]/p")); // 제품명
                        List<WebElement> productPrices = container.findElements(By.xpath(".//ul/li/div/div[1]/div[2]/div[2]/strong")); // 제품 가격
                        List<WebElement> productEvents = container.findElements(By.xpath(".//ul/li/div/div[2]/span")); // 행사 정보 (2+1, 1+1 등)
                        List<WebElement> productImages = container.findElements(By.xpath(".//ul/li/div/div[1]/div[1]/img")); // 이미지 URL

                        for (int i = 0; i < productNames.size(); i++) {
                            String name = productNames.get(i).getText();
                            String price = productPrices.size() > i ? productPrices.get(i).getText() : "가격 정보 없음";
                            int intPrice = Integer.parseInt(price.replaceAll("[^0-9]", ""));
                            String event;

                            if (productEvents.size() > i) {
                                String eventClass = productEvents.get(i).getAttribute("class");
                                if ("plus2".equals(eventClass)) {
                                    event = "2+1";
                                } else if ("plus1".equals(eventClass)) {
                                    event = "1+1";
                                } else {
                                    event = "행사 없음";
                                }
                            } else {
                                event = "행사 없음";
                            }

                            // 이미지 URL 추출
                            String imageUrl = productImages.size() > i ? productImages.get(i).getAttribute("src") : "이미지 없음";

                            // 이미지 URL이 상대 경로일 경우, 절대 경로로 변경
                            if (imageUrl.startsWith("//")) {
                                imageUrl = "https:" + imageUrl; // 프로토콜 추가
                            }

                            // ✅ 출력에 편의점(CU)과 카테고리 추가
                            System.out.println("편의점: CU | 분류: " + category + " | 제품명: " + name + " | 가격: " + intPrice + " | 행사: " + event + " | 이미지: " + imageUrl);
                        }
                    }

                    System.out.println("✅ 카테고리 [" + category + "] 크롤링 완료 ✅\n");

                    // ✅ 스크롤 최상단으로 이동
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("window.scrollTo(0, 0);");

                    // 다음 einfo 버튼으로 이동
                    einfoIndex++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit(); // 드라이버 종료
        }
    }
}
