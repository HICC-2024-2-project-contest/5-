package hello.core.member.controller;

import hello.core.crawling.dto.CrawlingDTO;
import hello.core.member.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/category/{productCategory}") // ✅ URL 매핑
    public String categoryPage(@PathVariable("productCategory") String productCategory, Model model) { // ✅ "productCategory" 명시적 지정
        System.out.println("🔹 요청된 카테고리: " + productCategory); // ✅ 디버깅 로그 추가

        List<CrawlingDTO> products = categoryService.getProductsByCategory(productCategory);

        if (products == null || products.isEmpty()) {
            System.out.println("⚠ 데이터 없음: " + productCategory); // ✅ 디버깅 로그 추가
        }

        model.addAttribute("productCategory", productCategory);
        model.addAttribute("products", products);
        return "category"; // ✅ Thymeleaf 템플릿 파일명이 "category.html"인지 확인
    }
}