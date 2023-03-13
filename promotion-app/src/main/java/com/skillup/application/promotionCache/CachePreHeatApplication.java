package com.skillup.application.promotionCache;


import com.skillup.application.promotionCache.util.CacheDomainMapper;
import com.skillup.domain.promotionSql.PromotionDomain;
import com.skillup.domain.promotionSql.PromotionService;
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
public class CachePreHeatApplication implements ApplicationRunner {
    @Autowired
    PromotionService promotionService;

    @Autowired
    PromotionCacheService promotionCacheService;

    @Autowired
    StockCacheService stockCacheService;

    // 当项目启动时，就会把run方法里的事情完成，所以把预热放在这里实现
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("---- Cache preheat Init active promotion stock into Cache ----");
        List<PromotionDomain> activePromotions =  promotionService.getPromotionByStatus(1);
        activePromotions.stream().forEach( promotionDomain -> {
            // 1. put promotion into cache
            promotionCacheService.updateCache(CacheDomainMapper.domainToCache(promotionDomain));
            // 2. put promotion available stock into cache
            stockCacheService.setAvailableStock(promotionDomain.getPromotionId(), promotionDomain.getAvailableStock());
        });
    }

}
