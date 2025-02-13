package hello.core.crawling.entity;

import hello.core.crawling.dto.CrawlingDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Entity
@Setter
@Getter
@Table(name = "crawling_table")
@IdClass(CrawlingEntity.CrawlingId.class)  // 복합 키 설정
public class CrawlingEntity {

    @Id
    @Column(nullable = false)
    private String companyName; // 1. 회사이름 (PK)

    @Id
    @Column(nullable = false)
    private String productNames; // 2. 상품명 (PK)

    @Column(nullable = false)
    private String productPrices; // 3. 가격

    @Column(nullable = false)
    private String productImages; // 4. 이미지 URL

    @Column
    private String discountInfo; // 5. 할인 정보

    @Column(nullable = false)
    private String productCategory; // 6. 상품 분류

    @Column
    private String barcode;  // 7. 바코드

    // CrawlingDTO → CrawlingEntity 변환 메서드
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

    // 복합 키 클래스 (Serializable 필수)
    @Getter
    @Setter
    public static class CrawlingId implements Serializable {
        private String companyName;
        private String productNames;

        public CrawlingId() {}

        public CrawlingId(String companyName, String productNames) {
            this.companyName = companyName;
            this.productNames = productNames;
        }

        // equals & hashCode 반드시 구현
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            CrawlingId that = (CrawlingId) obj;
            return companyName.equals(that.companyName) && productNames.equals(that.productNames);
        }

        @Override
        public int hashCode() {
            return companyName.hashCode() + productNames.hashCode();
        }
    }
}
