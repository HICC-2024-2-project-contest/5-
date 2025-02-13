package hello.core;

import hello.core.crawling.CUCrawling;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class CoreApplication implements CommandLineRunner {
	private final CUCrawling cuCrawling;

	public CoreApplication(CUCrawling cuCrawling) {
		this.cuCrawling = cuCrawling;
	}

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}

	@Override
	public void run(String... args) {
		cuCrawling.startCrawling(); // ✅ 크롤링 실행
	}
}