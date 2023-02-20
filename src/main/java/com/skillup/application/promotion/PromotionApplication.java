package com.skillup.application.promotion;

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
    PromotionService promotionService;

    @Autowired
    PromotionCacheService promotionCacheService;

    @Autowired
    StockCacheService stockCacheService;

    public PromotionDomain getPromotionById(String id) {
        // 1. try to hit in cache
        PromotionCacheDomain cachedPromotionDomain = promotionCacheService.getPromotionById(id);
        // 2. not hit, read from database
        if (Objects.isNull(cachedPromotionDomain)) {
            PromotionDomain promotionDomain = promotionService.getPromotionById(id);
            if (Objects.isNull(promotionDomain)) {
                return null;
            }
            // 3. update cache
            cachedPromotionDomain = promotionCacheService.updatePromotion(toCacheDomain(promotionDomain));
        }
        // TODO: update available stock
        Long availableStock = stockCacheService.getAvailableStock(cachedPromotionDomain.getPromotionId());
        cachedPromotionDomain.setAvailableStock(availableStock);
        // 4. return promotion domain
        return toPromotionDomain(cachedPromotionDomain);

    }

    private PromotionCacheDomain toCacheDomain(PromotionDomain promotionDomain) {
        return PromotionCacheDomain.builder()
                .promotionId(promotionDomain.getPromotionId())
                .promotionName(promotionDomain.getPromotionName())
                .commodityId(promotionDomain.getCommodityId())
                .startTime(promotionDomain.getStartTime())
                .endTime(promotionDomain.getEndTime())
                .originalPrice(promotionDomain.getOriginalPrice())
                .promotionalPrice(promotionDomain.getPromotionalPrice())
                .totalStock(promotionDomain.getTotalStock())
                .availableStock(promotionDomain.getAvailableStock())
                .lockStock(promotionDomain.getLockStock())
                .imageUrl(promotionDomain.getImageUrl())
                .status(promotionDomain.getStatus())
                .build();

    }

    private PromotionDomain toPromotionDomain(PromotionCacheDomain promotionCacheDomain) {
        return PromotionDomain.builder()
                .promotionId(promotionCacheDomain.getPromotionId())
                .promotionName(promotionCacheDomain.getPromotionName())
                .commodityId(promotionCacheDomain.getCommodityId())
                .startTime(promotionCacheDomain.getStartTime())
                .endTime(promotionCacheDomain.getEndTime())
                .originalPrice(promotionCacheDomain.getOriginalPrice())
                .promotionalPrice(promotionCacheDomain.getPromotionalPrice())
                .totalStock(promotionCacheDomain.getTotalStock())
                .availableStock(promotionCacheDomain.getAvailableStock())
                .lockStock(promotionCacheDomain.getLockStock())
                .imageUrl(promotionCacheDomain.getImageUrl())
                .status(promotionCacheDomain.getStatus())
                .build();
    }
}
