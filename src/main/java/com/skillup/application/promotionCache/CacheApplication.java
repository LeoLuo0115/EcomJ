package com.skillup.application.promotionCache;


import com.skillup.application.promotionCache.util.CacheDomainMapper;
import com.skillup.domian.promotionCache.PromotionCacheDomain;
import com.skillup.domian.promotionCache.PromotionCacheService;
import com.skillup.domian.promotionSql.PromotionDomain;
import com.skillup.domian.promotionSql.PromotionService;
import com.skillup.domian.stockCache.StockCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CacheApplication {

    @Autowired
    PromotionService promotionService;

    @Autowired
    PromotionCacheService promotionCacheService;

    @Autowired
    StockCacheService stockCacheService;



    public PromotionDomain getPromotionById(String promotionId) {
        // cache aside strategy
        // 1. try to hit cache
        PromotionCacheDomain promotionCacheDomain = promotionCacheService.getPromotionById(promotionId);

        // 2. not hit, query database
        if (Objects.isNull(promotionCacheDomain)) {
            PromotionDomain promotionDomain = promotionService.getPromotionById(promotionId);
            if (Objects.isNull(promotionDomain)) {
                return null;
            }

            // 3. update cache
            promotionCacheDomain = promotionCacheService.updateCache(CacheDomainMapper.domainToCache(promotionDomain));
        }

        /**
             4. return promotionDomain and update available stock,
             更新 cache里的 available stock（前端需要最新的数据），我们只更新这一个数据因为前端只需要最新的available stock
             方法 :
             1. 用 redis-lua 脚本 来更新缓存里的 available_stock, 不是更新 缓存里的 promotion
             2. 当前端要 get promotion 时, 把  available_stock 覆盖掉 promotion 旧的 available_stock

             注意: 只有 lock stock 和 revert stock 会操作 available stock, 所以要使用 redis-lua 在这两个方法里
             这里只设计缓存的修改，数据库 available_stock 不会改变，稍后需要引入 Rocketmq 来做到数据库异步修改
         */

        Long availableStock = stockCacheService.getAvailableStock(promotionCacheDomain.getPromotionId());
        promotionCacheDomain.setAvailableStock(availableStock);

        return CacheDomainMapper.cacheToDomain(promotionCacheDomain);
    }


}
