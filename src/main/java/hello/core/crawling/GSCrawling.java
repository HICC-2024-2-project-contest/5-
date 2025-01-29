package hello.core.crawling;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;


import java.util.List;

public class GSCrawling {

    public static void main(String[] args) throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.get("http://gs25.gsretail.com/gscvs/en/products/event-goods#;"); // 크롤링할 페이지 입력

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // 10초로 변경

        // ✅ TOTAL 버튼 클릭
        WebElement totalButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='TOTAL']")));
        totalButton.click();
        Thread.sleep(2000); // ✅ 페이지 로드 대기 (2초)

        // ✅ 상품 리스트 첫 번째 요소가 로드될 때까지 대기
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='wrap']/div[4]/div[2]/div[3]/div/div/div[1]/ul/li[1]")));

        while (true) {
            // 현재 페이지 번호 가져오기
            WebElement pageNumElement = driver.findElement(By.xpath("//*[@id='pageNum']"));
            String currentPage = pageNumElement.getAttribute("value"); // 현재 페이지 번호

            // 제품 목록 가져오기 (div[4]로 변경됨)
            List<WebElement> productContainers = driver.findElements(By.xpath("//*[@id='wrap']/div[4]/div[2]/div[3]/div/div/div[4]/ul/li"));

            for (WebElement container : productContainers) {
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

                // 이미지 URL이 상대 경로라면 절대 경로로 변환
                if (imageUrl.startsWith("//")) {
                    imageUrl = "https:" + imageUrl;
                }

                System.out.println("제품명: " + name + " | 가격: " + price + " | 행사: " + event + " | 이미지: " + imageUrl);
            }

            // Next 버튼 찾기
            WebElement nextButton = driver.findElement(By.xpath("//*[@id='wrap']/div[4]/div[2]/div[3]/div/div/div[4]/div/a[3]"));

            if (!nextButton.isDisplayed() || !nextButton.isEnabled()) {
                break; // 다음 버튼이 비활성화되면 종료
            }

            // Next 버튼 클릭
            nextButton.click();
            Thread.sleep(2000); // ✅ 페이지 로드 대기 (2초)

            // 페이지가 로드될 시간을 기다림
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='wrap']/div[4]/div[2]/div[3]/div/div/div[4]/ul/li[1]"))); // ✅ 첫 번째 제품이 나타날 때까지 대기

            // 새로운 페이지 번호 가져오기
            String newPage = driver.findElement(By.xpath("//*[@id='pageNum']")).getAttribute("value");

            // 페이지가 변하지 않았다면 마지막 페이지이므로 종료
            if (currentPage.equals(newPage)) {
                break;
            }
        }

        driver.quit();
    }
}
