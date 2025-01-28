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

public class SECrawling {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");

        // ChromeDriver 옵션 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        WebDriver driver = new ChromeDriver(options);

        try {
            // WebDriverWait 설정 (10초 대기)
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // 웹사이트 열기
            driver.get("https://www.7-eleven.co.kr/product/bestdosirakList.asp"); // 대상 웹사이트 URL 입력

            // "더보기" 버튼 클릭 반복 (버튼이 사라질 때까지)
            while (true) {
                try {
                    WebElement loadMoreButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='moreImg']/a")));
                    loadMoreButton.click(); // "더보기" 버튼 클릭
                    Thread.sleep(2000); // 로딩 대기
                } catch (Exception e) {
                    System.out.println("더보기 버튼을 찾을 수 없거나 모든 콘텐츠를 불러왔습니다.");
                    break;
                }
            }

            // 모든 제품명, 가격, 행사 정보, 이미지 URL 추출
            List<WebElement> productContainers = driver.findElements(By.xpath("//*[@id='listDiv']/div[2]/ul/li")); // 모든 제품 탐색

            for (WebElement container : productContainers) {
                // 제품명 추출
                List<WebElement> productNames = container.findElements(By.xpath(".//div/div/div[1]"));
                List<WebElement> productPrices = container.findElements(By.xpath(".//div/div/div[2]/span"));
                List<WebElement> productImages = container.findElements(By.xpath(".//div/img"));

                for (int i = 0; i < productNames.size(); i++) {
                    String name = productNames.get(i).getText(); // 제품명
                    String price = productPrices.size() > i ? productPrices.get(i).getText() : "가격 정보 없음"; // 가격
                    String imageUrl = productImages.size() > i ? productImages.get(i).getAttribute("src") : "이미지 없음"; // 이미지 URL

                    // 이미지 URL이 상대 경로일 경우, 절대 경로로 변경
                    if (imageUrl.startsWith("//")) {
                        imageUrl = "https:" + imageUrl; // 프로토콜 추가
                    }

                    System.out.println("제품명: " + name + " | 가격: " + price + " | 이미지: " + imageUrl);
                }
            }



        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit(); // 드라이버 종료
        }
    }
}
