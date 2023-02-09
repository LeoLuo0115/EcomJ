package com.skillup.domain.promotion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PromotionService {
    @Autowired
    PromotionRepository promotionRepository;

    @Resource(name = "${promotion.stock-strategy}")
    StockOperation stockOperation;
    public PromotionDomain registry(PromotionDomain promotionDomain) {
        promotionRepository.createPromotion(promotionDomain);
        return promotionDomain;
    }

    public PromotionDomain getPromotionById(String id) {
        return promotionRepository.getPromotionById(id);
    }

    public List<PromotionDomain> getPromotionByStatus(Integer status) {
        return promotionRepository.getPromotionByStatus(status);
    }

    public boolean lockStock(String id) {
        return stockOperation.lockStock(id);

    }

    public boolean deductStock(String id) {
        return true;
    }
}
