package com.skillup.domain.promotion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionDomainService {

    @Autowired
    PromotionRepository promotionRepository;

    public PromotionDomain createPromotion(PromotionDomain promotionDomain){
        promotionRepository.createPromotion(promotionDomain);
        return promotionDomain;
    }

    public PromotionDomain getPromotionById(String id){
        return promotionRepository.getPromotionById(id);
    }

    public List<PromotionDomain> getPromotionByStatus(Integer status) {
        return promotionRepository.getPromotionByStatus(status);
    }
}
