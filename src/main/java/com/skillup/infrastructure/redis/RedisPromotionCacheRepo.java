package com.skillup.infrastructure.redis;

import com.skillup.domain.promotionCache.PromotionCacheDomain;
import com.skillup.domain.promotionCache.PromotionCacheRepo;
import com.skillup.domain.stockCache.StockCacheRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import com.alibaba.fastjson2.JSON;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class RedisPromotionCacheRepo implements PromotionCacheRepo, StockCacheRepo {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Override
    public PromotionCacheDomain getPromotionById(String id) {
        return JSON.parseObject(get(id), PromotionCacheDomain.class);
    }

    @Override
    public void setPromotion(PromotionCacheDomain cacheDomain) {
        set(cacheDomain.getPromotionId(), cacheDomain);
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(value));
    }

    public String get(String key) {
        if (Objects.isNull(key)) return null;
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean lockStock(String id) {
        return false;
    }

    @Override
    public boolean revertStock(String id) {
        return false;
    }

    @Override
    public Long getPromotionAvailableStock(String promotionId) {
        return null;
    }

    @Override
    public void setPromotionAvailableStock(String promotionId, Long availableStock) {
        set(promotionId, availableStock);
    }
}
