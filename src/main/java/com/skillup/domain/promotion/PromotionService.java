package com.skillup.domain.promotion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromotionService {
    @Autowired
    PromotionRepository promotionRepository;
    public PromotionDomain createPromotion(PromotionDomain toDomain) {
        promotionRepository.createPromotion(promotionDomain);
        return promotionDomain;
    }

    public PromotionDomain getPromotionById(String id) {
        return null;
    }

    public List<PromotionDomain> getPromotionByStatus(Integer status) {
        return null;
    }
}
