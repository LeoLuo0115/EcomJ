package com.skillup.infrastructure.redis;

import com.alibaba.fastjson2.JSON;
import com.skillup.domain.promotionCache.PromotionCacheDomain;
import com.skillup.domain.promotionCache.PromotionCacheRepository;
import com.skillup.domain.stockCache.StockCacheDomain;
import com.skillup.domain.stockCache.StockCacheRepository;
import com.skillup.domain.stockCache.StockOrderDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

@Slf4j
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
                log.info("Redis: lock-stock successfully");
                return true;
            } else {
                // -1 means sold out, -2 promotion doesn't exist
                log.info("Redis: lock-stock failed");
                return false;
            }
        } catch (Throwable throwable) {
            log.info("Redis: lock-stock throw error");
            throw new RuntimeException(throwable);
        }
    }

    @Override
    public boolean revertStock(StockOrderDomain stockOrderDomain) {
        try {
            Long stock = redisTemplate.execute(redisLockStockScript,
                    Arrays.asList(
                            StockCacheDomain.createStockKey(stockOrderDomain.getPromotionId()),
                            stockOrderDomain.getOrderNum().toString(),
                            stockOrderDomain.getOperationName().toString()
                    ));

            if (stock >= 0) {
                // log.info("Redis: revert-stock successfully");
                return true;
            } else {
                // -1 means sold out, -2 promotion doesn't exist
                log.info("Redis: revert-stock failed");
                return false;
            }
        } catch (Throwable throwable) {
            // log.info("Redis: revert-stock throw error");
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
