package com.skillup.application.promotion.util;

import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotionCache.PromotionCacheDomain;

public class CacheDomainMapper {
    public static PromotionCacheDomain toCacheDomain(PromotionDomain promotionDomain) {
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

    public static PromotionDomain toPromotionDomain(PromotionCacheDomain promotionCacheDomain) {
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
