package com.skillup.domain.promotionCache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromotionCacheService {

    @Autowired
    PromotionCacheRepository promotionCacheRepository;
    public PromotionCacheDomain getPromotionById(String id) {
        return promotionCacheRepository.getPromotionById(id);
    }

    public PromotionCacheDomain updatePromotion(PromotionCacheDomain promotionCacheDomain) {
        promotionCacheRepository.updatePromotion(promotionCacheDomain);
        return promotionCacheDomain;
    }
}
