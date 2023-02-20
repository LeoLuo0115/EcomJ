package com.skillup.infrastructure.redis;

import com.alibaba.fastjson2.JSON;
import com.skillup.domain.promotionCache.PromotionCacheDomain;
import com.skillup.domain.promotionCache.PromotionCacheRepository;
import com.skillup.domain.stockCache.StockCacheDomain;
import com.skillup.domain.stockCache.StockCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Objects;

@Repository
public class RedisPromotionCacheRepo implements PromotionCacheRepository, StockCacheRepository {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Autowired
    DefaultRedisScript<Long> redisLockStockScript;

    @Autowired
    DefaultRedisScript<Long> redisRevertStockScript;

    @Override
    public PromotionCacheDomain getPromotionById(String id) {
        return JSON.parseObject(get(id), PromotionCacheDomain.class);
    }

    @Override
    public void updatePromotion(PromotionCacheDomain promotionCacheDomain) {
        set(promotionCacheDomain.getPromotionId(), promotionCacheDomain);
    }

    @Override
    public boolean lockStock(String id) {
        // 0 Lua script to ACID below operations
        // 1 select form available_stock = ?
        // 2 if available_stock > 0 then update available_stock = available_stock - 1
        try {
            Long stock = redisTemplate.execute(redisLockStockScript, Collections.singletonList(StockCacheDomain.createStockKey(id)));
            if (stock >= 0) {
                return true;
            } else {
                /**
                 * -1: sold out
                 * -2: promotion does not exist
                 */
                return false;
            }
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @Override
    public boolean revertStock(String id) {
        try {
            Long stock = redisTemplate.execute(redisRevertStockScript, Collections.singletonList(StockCacheDomain.createStockKey(id)));
            if (stock > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @Override
    public Long getAvailableStockById(String promotionId) {
        String stockKey = StockCacheDomain.createStockKey(promotionId);
        return JSON.parseObject(get(stockKey), Long.class);
    }

    @Override
    public void setPromotionAvailableStock(String promotionId, Long availableStock) {
        String stockKey = StockCacheDomain.createStockKey(promotionId);
        set(stockKey, availableStock);

    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(value));
    }

    public String get(String key) {
        if (Objects.isNull(key)) return null;
        return redisTemplate.opsForValue().get(key);
    }


}
