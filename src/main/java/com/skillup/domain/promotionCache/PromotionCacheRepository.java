package com.skillup.domain.promotionCache;

public interface PromotionCacheRepository {
    PromotionCacheDomain getPromotionById(String id);

    void updatePromotion(PromotionCacheDomain promotionCacheDomain);
}
