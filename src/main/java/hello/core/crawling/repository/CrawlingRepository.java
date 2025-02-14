package hello.core.crawling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import hello.core.crawling.entity.CrawlingEntity;

public interface CrawlingRepository extends JpaRepository<CrawlingEntity, CrawlingEntity.CrawlingId> {
}