package hello.core.member.repository;

import hello.core.crawling.entity.CrawlingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<CrawlingEntity, Long> {
    // ✅ product_category 컬럼을 기준으로 조회
    List<CrawlingEntity> findByProductCategory(String productCategory);
}
