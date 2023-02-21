package com.skillup.infrastructure.redis;

import com.alibaba.fastjson2.JSON;
import com.skillup.domian.promotionCache.PromotionCacheDomain;
import com.skillup.domian.promotionCache.PromotionCacheRepository;
import com.skillup.domian.stockCache.StockCacheDomain;
import com.skillup.domian.stockCache.StockCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Objects;

@Repository
public class RedisPromotionCacheRepo implements PromotionCacheRepository, StockCacheRepository {
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Resource(name = "lock")
    DefaultRedisScript<Long> redisLockStockScript;


    @Resource(name = "revert")
    DefaultRedisScript<Long> redisRevertStockScrip;


    @Override
    public PromotionCacheDomain getCachedPromotion(String promotionId) {
        return JSON.parseObject(get(promotionId), PromotionCacheDomain.class);
    }

    @Override
    public void setCachedPromotion(PromotionCacheDomain promotionCacheDomain) {
        set(promotionCacheDomain.getPromotionId(), promotionCacheDomain);
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(value));
    }

    public String get(String key) {
        if (Objects.isNull(key)) return null;
        return redisTemplate.opsForValue().get(key);
    }


    // Stock Cache method implementation
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
                // -1 means sold out, -2 promotion doesn't exist
                return false;
            }
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @Override
    public boolean revertStock(String id) {
        try {
            Long stock = redisTemplate.execute(redisRevertStockScrip, Collections.singletonList(StockCacheDomain.createStockKey(id)));
            if (stock >= 0) {
                return true;
            } else {
                // -1 means sold out, -2 promotion doesn't exist
                return false;
            }
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @Override
    public Long getPromotionAvailableStock(String promotionId) {
        String stockKey = StockCacheDomain.createStockKey(promotionId);
        return JSON.parseObject(get(stockKey), Long.class);
    }

    @Override
    public void setPromotionAvailableStock(String promotionId, Long availableStock) {
        String stockKey = StockCacheDomain.createStockKey(promotionId);
        set(stockKey, availableStock);
    }
}
