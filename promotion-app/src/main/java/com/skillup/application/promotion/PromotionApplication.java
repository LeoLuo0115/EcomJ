package com.skillup.application.promotion;

import com.skillup.application.promotion.util.CacheDomainMapper;
import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionService;
import com.skillup.domain.promotionCache.PromotionCacheDomain;
import com.skillup.domain.promotionCache.PromotionCacheService;
import com.skillup.domain.stockCache.StockCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PromotionApplication {
    @Autowired
    PromotionCacheService promotionCacheService;

    @Autowired
    PromotionService promotionService;

    @Autowired
    StockCacheService stockCacheService;

    public PromotionDomain getPromotionById(String id) {
        // cache aside read strategy
        // 1 try to hit cache
        PromotionCacheDomain cachedPromotion = promotionCacheService.getPromotionById(id);
        // 2 not hit, read database
        if (Objects.isNull(cachedPromotion)) {
            PromotionDomain promotionDomain = promotionService.getPromotionById(id);
            if (Objects.isNull(promotionDomain)) {
                return null;
            }
            // 3. update cache
            cachedPromotion = CacheDomainMapper.domainToCache(promotionDomain);
            promotionCacheService.updatePromotion(cachedPromotion);
        }
        // 4. update available stock, return promotionDomain
        Long availableStock = stockCacheService.getAvailableStock(cachedPromotion.getPromotionId());
        cachedPromotion.setAvailableStock(availableStock);
        return CacheDomainMapper.cacheToDomain(cachedPromotion);
    }


}
