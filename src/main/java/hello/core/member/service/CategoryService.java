package hello.core.member.service;

import hello.core.crawling.dto.CrawlingDTO;
import hello.core.crawling.entity.CrawlingEntity;
import hello.core.member.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CrawlingDTO> getProductsByCategory(String productCategory) {
        List<CrawlingEntity> entityList = categoryRepository.findByProductCategory(productCategory);

        // ✅ CrawlingEntity 리스트를 CrawlingDTO 리스트로 변환
        return entityList.stream()
                .map(CrawlingDTO::toCrawlingDTO) // CrawlingDTO의 변환 메서드 사용
                .collect(Collectors.toList());
    }
}
