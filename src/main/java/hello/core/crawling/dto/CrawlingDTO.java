package hello.core.crawling.dto;

import hello.core.crawling.entity.CrawlingEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CrawlingDTO {
    private Long id;
    private String companyName;
    private String productNames;
    private String productPrices;
    private String productImages;
    private String discountInfo;
    private String productCategory;
    private String barcode;

    // Entity → DTO 변환 메서드
    public static CrawlingDTO toCrawlingDTO(CrawlingEntity crawlingEntity) {
        CrawlingDTO crawlingDTO = new CrawlingDTO();
        crawlingDTO.setId(crawlingEntity.getId());


        crawlingDTO.setProductNames(crawlingEntity.getProductNames());
        crawlingDTO.setProductPrices(crawlingEntity.getProductPrices());
        crawlingDTO.setProductImages(crawlingEntity.getProductImages());
        crawlingDTO.setDiscountInfo(crawlingEntity.getDiscountInfo());
        crawlingDTO.setProductCategory(crawlingEntity.getProductCategory());
        crawlingDTO.setBarcode(crawlingEntity.getBarcode());
        crawlingDTO.setCompanyName(crawlingEntity.getCompanyName());
        return crawlingDTO;

    }

    // DTO → Entity 변환 메서드
    public CrawlingEntity toEntity() {
        CrawlingEntity entity = new CrawlingEntity();
        entity.setId(this.id);
        entity.setCompanyName(this.companyName);
        entity.setProductNames(this.productNames);
        entity.setProductPrices(this.productPrices);
        entity.setProductImages(this.productImages);
        entity.setDiscountInfo(this.discountInfo);
        entity.setProductCategory(this.productCategory);
        entity.setBarcode(this.barcode);
        return entity;
    }
}