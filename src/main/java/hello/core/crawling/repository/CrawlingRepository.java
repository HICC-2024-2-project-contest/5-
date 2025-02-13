package hello.core.crawling.repository;

import hello.core.crawling.entity.CrawlingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrawlingRepository extends JpaRepository<CrawlingEntity, Long> {
}