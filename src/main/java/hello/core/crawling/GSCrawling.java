package hello.core.crawling;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class GSCrawling {

        public static void main(String[] args) {
            // WebDriver 경로 설정 (ChromeDriver 다운로드 필요)
            System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");

            // WebDriver 초기화
            WebDriver driver = new ChromeDriver();

            try {
                // GS25 이벤트 상품 페이지 URL
                String url = "http://gs25.gsretail.com/gscvs/en/products/event-goods#";
                driver.get(url);

                // JavaScript 실행으로 이미지 태그 가져오기
                JavascriptExecutor js = (JavascriptExecutor) driver;
                List<Object> imageUrls = (List<Object>) js.executeScript(
                        "return Array.from(document.querySelectorAll('.evtProduct img')).map(img => img.src);"
                );

                // 이미지 URL 출력
                System.out.println("이미지 URL 리스트:");
                for (Object imageUrl : imageUrls) {
                    System.out.println(imageUrl.toString());
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                // WebDriver 종료
                driver.quit();
            }
        }
    }

