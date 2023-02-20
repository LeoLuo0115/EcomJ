package com.skillup.application.promotion;

import com.skillup.application.promotion.util.CacheDomainMapper;
import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionService;
import com.skillup.domain.promotionCache.PromotionCacheService;
import com.skillup.domain.stockCache.StockCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PromotionPreHeatApplication implements ApplicationRunner {

    @Autowired
    PromotionService promotionService;

    @Autowired
    PromotionCacheService promotionCacheService;

    @Autowired
    StockCacheService stockCacheService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("---- Cache preheat Init active promotion stock into Cache ----");
        List<PromotionDomain> activePromotion = promotionService.getPromotionByStatus(1);
        activePromotion.stream().forEach(promotionDomain -> {
            // 1. update promotion info to promotion cache
            promotionCacheService.updatePromotion(CacheDomainMapper.toCacheDomain(promotionDomain));
            // 2. update promotion stock info to stock cache
            stockCacheService.setAvailableStock(promotionDomain.getPromotionId(), promotionDomain.getAvailableStock());
        });

    }
}
