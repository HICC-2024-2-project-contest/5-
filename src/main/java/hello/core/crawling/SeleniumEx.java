package hello.core.crawling;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumEx {
    public static void main(String[] args) {
        // WebDriver 경로 설정 (ChromeDriver 다운로드 필요)
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");

        // WebDriver 초기화
        WebDriver driver = new ChromeDriver();

        try {
            String url = "http://gs25.gsretail.com/gscvs/en/products/event-goods#";
            driver.get(url);

            // <title> 태그 가져오기
            String pageTitle = driver.getTitle();

            // 페이지 제목 출력
            System.out.println("페이지 제목: " + pageTitle);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // WebDriver 종료
            driver.quit();
        }
    }
}
