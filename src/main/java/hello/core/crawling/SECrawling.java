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

public class SECrawling {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        WebDriver driver = new ChromeDriver(options);

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // 1. 첫 번째 사이트 크롤링
            driver.get("https://www.7-eleven.co.kr/product/bestdosirakList.asp");

            while (true) {
                try {
                    WebElement loadMoreButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='moreImg']/a")));
                    loadMoreButton.click();
                    Thread.sleep(2000);
                } catch (Exception e) {
                    System.out.println("첫 번째 사이트: 더보기 버튼을 찾을 수 없거나 모든 콘텐츠를 불러왔습니다.");
                    break;
                }
            }

            List<WebElement> productContainers = driver.findElements(By.xpath("//*[@id='listDiv']/div[2]/ul/li"));

            for (WebElement container : productContainers) {
                List<WebElement> productNames = container.findElements(By.xpath(".//div/div/div[1]"));
                List<WebElement> productPrices = container.findElements(By.xpath(".//div/div/div[2]/span"));
                List<WebElement> productImages = container.findElements(By.xpath(".//div/img"));

                for (int i = 0; i < productNames.size(); i++) {
                    String name = productNames.get(i).getText();
                    String price = productPrices.size() > i ? productPrices.get(i).getText() : "가격 정보 없음";
                    int intPrice = Integer.parseInt(price.replaceAll("[^0-9]", ""));
                    String event = "행사 없음";
                    String imageUrl = productImages.size() > i ? productImages.get(i).getAttribute("src") : "이미지 없음";

                    if (imageUrl.startsWith("//")) {
                        imageUrl = "https:" + imageUrl;
                    }

                    String category = "";

                    System.out.println("편의점: SE | 분류: " + category + " | 제품명: " + name + " | 가격: " + intPrice + " | 행사: " + event + " | 이미지: " + imageUrl);
                }
            }

            // 2. 두 번째 사이트 크롤링
            driver.get("https://www.7-eleven.co.kr/product/presentList.asp");

            // "더보기" 버튼 처리
            for (int j=0; j<2; j++) {
                if (j == 1) {
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("window.scrollTo(0, 0);");
                    WebElement nextTabButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"actFrm\"]/div[3]/div[2]/ul/li[2]/a")));
                    nextTabButton.click();
                }
                boolean firstClick = true;
                while (true) {
                    try {
                        String moreButtonXPath = firstClick ? "//*[@id='listUl']/li[15]/a" : "//*[@id='moreImg']/a";
                        WebElement loadMoreButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(moreButtonXPath)));
                        loadMoreButton.click();
                        Thread.sleep(2000);
                        firstClick = false;
                    } catch (Exception e) {
                        System.out.println("두 번째 사이트: 더보기 버튼을 찾을 수 없거나 모든 콘텐츠를 불러왔습니다.");
                        break;
                    }
                }

                List<WebElement> productContainers2 = driver.findElements(By.xpath("//*[@id='listUl']/li"));

                for (int i = 0; i < productContainers2.size(); i++) {
                    WebElement container = productContainers2.get(i);

                    // data-index 속성 가져오기
                    String dataIndex = container.getAttribute("data-index");
                    int index;

                    try {
                        index = (dataIndex != null && !dataIndex.isEmpty()) ? Integer.parseInt(dataIndex) : i + 1;
                    } catch (NumberFormatException e) {
                        index = i + 1; // 예외 발생 시 기본값 사용
                    }

                    // 제품명 XPath 분기
                    String nameXPath = index <= 14 ? ".//div/div/div[1]" : ".//div/div/div/div[1]";
                    // 가격 XPath 분기
                    String priceXPath = index <= 14 ? ".//div/div/div[2]/span" : ".//div/div/div/div[2]/span";
                    // 행사 XPath 분기
                    String eventXPath = index <= 14 ? ".//ul/li[1]" : ".//ul/li";
                    // 이미지 XPath 분기
                    String imageXPath = index <= 14 ? ".//div/img" : ".//div/div/img";

                    try {
                        WebElement nameElement = container.findElement(By.xpath(nameXPath));
                        WebElement priceElement = container.findElement(By.xpath(priceXPath));
                        List<WebElement> eventElements = container.findElements(By.xpath(eventXPath));
                        WebElement imageElement = container.findElement(By.xpath(imageXPath));

                        String name = nameElement.getText();
                        String priceText = priceElement.getText();
                        int intPrice = priceText.isEmpty() ? 0 : Integer.parseInt(priceText.replaceAll("[^0-9]", ""));
                        String event = eventElements.isEmpty() ? "행사 없음" : eventElements.get(0).getText();
                        String imageUrl = imageElement.getAttribute("src");

                        if (imageUrl.startsWith("//")) {
                            imageUrl = "https:" + imageUrl;
                        }

                        String category = "";
                        System.out.println("편의점: SE | 분류: " + category + " | 제품명: " + name + " | 가격: " + intPrice + " | 행사: " + event + " | 이미지: " + imageUrl);
                    } catch (Exception e) {
                        System.out.println("오류 발생: " + e.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
