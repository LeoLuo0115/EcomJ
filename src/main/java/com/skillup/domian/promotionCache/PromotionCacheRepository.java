package com.skillup.domian.promotionCache;

public interface PromotionCacheRepository {

    public PromotionCacheDomain getCachedPromotion(String promotionId);

    public void setCachedPromotion(PromotionCacheDomain promotionCacheDomain);
}
