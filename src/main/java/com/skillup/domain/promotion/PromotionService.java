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

    public PromotionDomain createPromotion(PromotionDomain promotionDomain) {
        promotionRepository.createPromotion(promotionDomain);
        return promotionDomain;
    }

    public PromotionDomain getPromotionById(String id) {
        return promotionRepository.getPromotionByPromotionId(id);
    }

    public List<PromotionDomain> getPromotionByStatus(Integer status) {
        return promotionRepository.getPromotionByPromotionStatus(status);
    }

    public boolean lockStock(String id) {
        return stockOperation.lockStock(id);
    }

    public boolean deductStock(String id) {
        return stockOperation.deductStock(id);
    }

    public boolean revertStock(String id) {
        return stockOperation.revertStock(id);
    }
}

