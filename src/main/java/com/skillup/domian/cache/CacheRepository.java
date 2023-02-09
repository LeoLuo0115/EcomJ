package com.skillup.domian.cache;

public interface CacheRepository {

    public CacheDomain getCachedPromotion(String promotionId);

    public void setCachedPromotion(CacheDomain cacheDomain);
}
