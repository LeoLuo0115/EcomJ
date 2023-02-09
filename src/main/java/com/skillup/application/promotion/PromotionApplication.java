package com.skillup.application.promotion;


import com.skillup.application.promotion.util.CacheDomainMapper;
import com.skillup.domian.cache.CacheDomain;
import com.skillup.domian.cache.CacheService;
import com.skillup.domian.promotion.PromotionDomain;
import com.skillup.domian.promotion.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PromotionApplication {

    @Autowired
    PromotionService promotionService;

    @Autowired
    CacheService cacheService;


    public PromotionDomain getPromotionById(String promotionId) {
        // 1. try to hit cache
        CacheDomain cacheDomain = cacheService.getPromotionById(promotionId);

        // 2. not hit, query database
        if (Objects.isNull(cacheDomain)) {
            PromotionDomain promotionDomain = promotionService.getPromotionById(promotionId);
            if (Objects.isNull(promotionDomain)) {
                return null;
            }

            // 3. update cache
            cacheDomain = cacheService.updateCache(CacheDomainMapper.domainToCache(promotionDomain));
        }

        // 4. return promotionDomain
        return CacheDomainMapper.cacheToDomain(cacheDomain);
    }


}
