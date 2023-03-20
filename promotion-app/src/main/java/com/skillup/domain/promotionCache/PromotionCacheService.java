package com.skillup.domain.promotionCache;


import com.skillup.infrastructure.redis.RedisPromotionCacheRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromotionCacheService {
    @Autowired
    RedisPromotionCacheRepo redisCacheRepo;
    public PromotionCacheDomain getPromotionById(String promotionId) {

        return redisCacheRepo.getCachedPromotion(promotionId);
    }

    public PromotionCacheDomain updateCache(PromotionCacheDomain promotionCacheDomain) {
        redisCacheRepo.setCachedPromotion(promotionCacheDomain);
        return promotionCacheDomain;
    }
}
