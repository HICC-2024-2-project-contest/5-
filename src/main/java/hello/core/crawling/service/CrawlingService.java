package hello.core.crawling.service;


import hello.core.crawling.dto.CrawlingDTO;

import hello.core.crawling.entity.CrawlingEntity;
import hello.core.crawling.repository.CrawlingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CrawlingService {
    private final CrawlingRepository crawlingRepository;

    public void saveEntity(CrawlingEntity crawlingEntity) {
        crawlingRepository.save(crawlingEntity);
    }
}