package hello.core.crawling;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.List;


public class SeleniumEx {
    public static void main(String[] args) {
        // WebDriver 경로 설정 (ChromeDriver 다운로드 필요)
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");

        // WebDriver 초기화
        WebDriver driver = new ChromeDriver();

        try {

            String url = "https://quotes.toscrape.com/";
            driver.get(url);

            // 페이지의 모든 인용문 요소 가져오기
            List<WebElement> quotes = driver.findElements(By.className("quote"));

            for (WebElement quote : quotes) {
                // 인용문 텍스트와 저자 가져오기
                String quoteText = quote.findElement(By.className("text")).getText();
                String author = quote.findElement(By.className("author")).getText();

                // 출력
                System.out.println("인용문: " + quoteText);
                System.out.println("저자: " + author);
                System.out.println("---------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // WebDriver 종료
            driver.quit();
        }
    }
}