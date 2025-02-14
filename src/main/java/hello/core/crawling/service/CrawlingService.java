package hello.core.crawling.service;


import hello.core.crawling.dto.CrawlingDTO;
import hello.core.crawling.entity.CrawlingEntity;
import hello.core.crawling.entity.CrawlingEntity.CrawlingId;
import hello.core.crawling.repository.CrawlingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class CrawlingService {

    private final CrawlingRepository crawlingRepository;

    public CrawlingService(CrawlingRepository crawlingRepository) {
        this.crawlingRepository = crawlingRepository;
    }

    @Transactional
    public CrawlingEntity saveOrUpdate(CrawlingDTO crawlingDTO) {
        CrawlingId crawlingId = new CrawlingId(crawlingDTO.getCompanyName(), crawlingDTO.getProductNames());

        Optional<CrawlingEntity> existingEntity = crawlingRepository.findById(crawlingId);
        CrawlingEntity entity;

        if (existingEntity.isPresent()) {
            entity = existingEntity.get(); // 기존 데이터 수정
            entity.setProductPrices(crawlingDTO.getProductPrices());
            entity.setProductImages(crawlingDTO.getProductImages());
            entity.setDiscountInfo(crawlingDTO.getDiscountInfo());
            entity.setProductCategory(crawlingDTO.getProductCategory());
            entity.setBarcode(crawlingDTO.getBarcode());
        } else {
            entity = CrawlingEntity.toCrawlingEntity(crawlingDTO); // 새로운 데이터 삽입
        }

        return crawlingRepository.save(entity);
    }
}
