package com.skillup.application.promotion.util;

import com.skillup.domian.cache.CacheDomain;
import com.skillup.domian.promotion.PromotionDomain;

public class CacheDomainMapper {
    public static CacheDomain domainToCache(PromotionDomain domain) {
        return CacheDomain.builder()
                .promotionId(domain.getPromotionId())
                .promotionName(domain.getPromotionName())
                .commodityId(domain.getCommodityId())
                .startTime(domain.getStartTime())
                .endTime(domain.getEndTime())
                .originalPrice(domain.getOriginalPrice())
                .promotionalPrice(domain.getPromotionalPrice())
                .totalStock(domain.getTotalStock())
                .availableStock(domain.getAvailableStock())
                .lockStock(domain.getLockStock())
                .imageUrl(domain.getImageUrl())
                .status(domain.getStatus())
                .build();
    }

    public static PromotionDomain cacheToDomain(CacheDomain cache) {
        return PromotionDomain.builder()
                .promotionId(cache.getPromotionId())
                .promotionName(cache.getPromotionName())
                .commodityId(cache.getCommodityId())
                .startTime(cache.getStartTime())
                .endTime(cache.getEndTime())
                .originalPrice(cache.getOriginalPrice())
                .promotionalPrice(cache.getPromotionalPrice())
                .totalStock(cache.getTotalStock())
                .availableStock(cache.getAvailableStock())
                .lockStock(cache.getLockStock())
                .imageUrl(cache.getImageUrl())
                .status(cache.getStatus())
                .build();
    }
}
