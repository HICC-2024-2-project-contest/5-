package hello.core;

import hello.core.crawling.CUCrawling;
import hello.core.crawling.GSCrawling;
import hello.core.crawling.SECrawling;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class CoreApplication implements CommandLineRunner {
	private final CUCrawling cuCrawling;
	private final GSCrawling gsCrawling;
	private final SECrawling seCrawling;

	public CoreApplication(CUCrawling cuCrawling, GSCrawling gsCrawling, SECrawling seCrawling) {
		this.cuCrawling = cuCrawling;
		this.gsCrawling = gsCrawling;
		this.seCrawling = seCrawling;
	}

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}

	@Override
	public void run(String... args) {
		seCrawling.startCrawling();
		gsCrawling.startCrawling();
		cuCrawling.startCrawling(); // ✅ 크롤링 실행
	}
}