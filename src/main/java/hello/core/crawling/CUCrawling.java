package hello.core.crawling;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

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
            // 웹사이트 열기
            driver.get("https://cu.bgfretail.com/product/product.do?category=product&depth2=4&depth3=4"); // 여기에 대상 웹사이트 URL 입력

            // WebDriverWait 설정 (10초 대기)
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            // "더보기" 버튼 클릭 반복
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

            // 모든 제품명, 가격, 행사 정보, 이미지 URL 추출
            List<WebElement> productContainers = driver.findElements(By.xpath("//*[@id='dataTable']/div")); // 모든 div 탐색
            for (WebElement container : productContainers) {
                List<WebElement> productNames = container.findElements(By.xpath(".//ul/li/div/div[1]/div[2]/div[1]/p")); // 제품명
                List<WebElement> productPrices = container.findElements(By.xpath(".//ul/li/div/div[1]/div[2]/div[2]/strong")); // 제품 가격
                List<WebElement> productEvents = container.findElements(By.xpath(".//ul/li/div/div[2]/span")); // 행사 정보 (2+1, 1+1 등)
                List<WebElement> productImages = container.findElements(By.xpath(".//ul/li/div/div[1]/div[1]/img")); // 이미지 URL

                for (int i = 0; i < productNames.size(); i++) {
                    String name = productNames.get(i).getText();
                    String price = productPrices.size() > i ? productPrices.get(i).getText() : "가격 정보 없음";
                    String event;

                    if (productEvents.size() > i) {
                        String eventClass = productEvents.get(i).getAttribute("class");
                        if ("plus2".equals(eventClass)) {
                            event = "2+1 행사";
                        } else if ("plus1".equals(eventClass)) {
                            event = "1+1 행사";
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

                    System.out.println("제품명: " + name + " | 가격: " + price + " | 행사: " + event + " | 이미지: " + imageUrl);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit(); // 드라이버 종료
        }
    }
}
