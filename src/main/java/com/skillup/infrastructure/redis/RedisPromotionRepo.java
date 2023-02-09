package com.skillup.infrastructure.redis;

import com.alibaba.fastjson2.JSON;
import com.skillup.domian.cache.CacheDomain;
import com.skillup.domian.cache.CacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;

public class RedisPromotionRepo implements CacheRepository {
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Override
    public CacheDomain getCachedPromotion(String promotionId) {
        return JSON.parseObject(get(promotionId), CacheDomain.class);
    }

    @Override
    public void setCachedPromotion(CacheDomain cacheDomain) {
        set(cacheDomain.getPromotionId(), cacheDomain);
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(value));
    }

    public String get(String key) {
        if (Objects.isNull(key)) return null;
        return redisTemplate.opsForValue().get(key);
    }
}
