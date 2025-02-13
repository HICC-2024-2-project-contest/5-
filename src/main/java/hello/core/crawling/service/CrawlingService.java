package hello.core.crawling.service;

import hello.core.crawling.entity.CrawlingEntity;
import hello.core.crawling.repository.CrawlingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CrawlingService {

    @Autowired
    private CrawlingRepository crawlingRepository;

    // 크롤링 데이터를 저장하는 메서드
    public void saveCrawlingData(String companyName, String productName, String productPrice, String productImage, String discountInfo, String productCategory, String barcode) {
        CrawlingEntity entity = new CrawlingEntity();
        entity.setCompanyName(companyName);
        entity.setProductNames(productName);
        entity.setProductPrices(productPrice);
        entity.setProductImages(productImage);
        entity.setDiscountInfo(discountInfo);
        entity.setProductCategory(productCategory);
        entity.setBarcode(barcode);

        crawlingRepository.save(entity);
    }

    // 모든 크롤링 데이터 조회
    public List<CrawlingEntity> getAllCrawlingData() {
        return crawlingRepository.findAll();
    }
}