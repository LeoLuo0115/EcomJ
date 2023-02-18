package com.skillup.infrastructure.redis;

import com.alibaba.fastjson2.JSON;
import com.skillup.domain.promotionCache.PromotionCacheDomain;
import com.skillup.domain.promotionCache.PromotionCacheRepo;
import com.skillup.domain.stockCache.StockCacheDomain;
import com.skillup.domain.stockCache.StockCacheRepo;
import com.skillup.domain.stockCache.StockOrderDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
public class RedisPromotionCacheRepo implements PromotionCacheRepo, StockCacheRepo {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Autowired
    DefaultRedisScript<Long> redisLockStockScript;

    @Autowired
    DefaultRedisScript<Long> redisRevertStockScript;

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(value));
    }

    public String get(String key) {
        if (Objects.isNull(key)) return null;
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public PromotionCacheDomain getPromotionById(String id) {
        return JSON.parseObject(get(id), PromotionCacheDomain.class);
    }

    @Override
    public void setPromotion(PromotionCacheDomain cacheDomain) {
        set(cacheDomain.getPromotionId(), cacheDomain);
    }

    @Override
    public boolean lockStock(StockOrderDomain stockOrderDomain) {
        // 0 Lua script to ACID below operations
        // 1 select form available_stock = ?
        // 2 if available_stock > 0 then update available_stock = available_stock - 1
        try {
            Long stock = redisTemplate.execute(redisLockStockScript,
                    Arrays.asList(
                            StockCacheDomain.createStockKey(stockOrderDomain.getPromotionId()),
                            stockOrderDomain.getOrderNum().toString(),
                            stockOrderDomain.getOperationName().toString()
                            ));
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
    public boolean revertStock(StockOrderDomain stockOrderDomain) {
        try {
            Long stock = redisTemplate.execute(redisRevertStockScript, Arrays.asList(
                    StockCacheDomain.createStockKey(stockOrderDomain.getPromotionId()),
                    stockOrderDomain.getOrderNum().toString(),
                    stockOrderDomain.getOperationName().toString()
            ));
            if (stock > 0) {
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
