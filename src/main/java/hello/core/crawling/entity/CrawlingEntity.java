package hello.core.crawling.entity;

import hello.core.crawling.dto.CrawlingDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "crawling_table")
public class CrawlingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본키 추가

    @Column(nullable = false)
    private String companyName; // 1. 회사이름

    @Column(nullable = false)
    private String productNames; // 2. 상품명

    @Column(nullable = false)
    private String productPrices; // 3. 가격

    @Column(nullable = false)
    private String productImages; // 4. 이미지 URL

    @Column
    private String discountInfo; // 5. 할인 정보

    @Column(nullable = false)
    private String productCategory; // 6. 상품 분류

    @Column(unique = true, nullable = false)
    private String barcode;  // 7. 바코드 (대문자 → 소문자로 수정)

    // CrawlingDTO → CrawlingEntity 변환 메서드 개선
    public static CrawlingEntity toCrawlingEntity(CrawlingDTO crawlingDTO) {
        CrawlingEntity entity = new CrawlingEntity();
        entity.setCompanyName(crawlingDTO.getCompanyName());
        entity.setProductNames(crawlingDTO.getProductNames());
        entity.setProductPrices(crawlingDTO.getProductPrices());
        entity.setProductImages(crawlingDTO.getProductImages());
        entity.setDiscountInfo(crawlingDTO.getDiscountInfo());
        entity.setProductCategory(crawlingDTO.getProductCategory());
        entity.setBarcode(crawlingDTO.getBarcode());
        return entity;
    }
}