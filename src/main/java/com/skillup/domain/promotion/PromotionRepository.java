package com.skillup.domain.promotion;

import java.util.List;

public interface PromotionRepository {
    void createPromotion(PromotionDomain promotionDomain);

    PromotionDomain getPromotionById(String id);

    List<PromotionDomain> getPromotionByStatus(Integer status);

    void updatePromotion(PromotionDomain promotionById);
    //update mei xie implementation
}
