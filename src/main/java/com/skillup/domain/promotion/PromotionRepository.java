package com.skillup.domain.promotion;


import java.util.List;

public interface PromotionRepository {

    void createPromotion(PromotionDomain promotionDomain);

    PromotionDomain getPromotionByPromotionId(String promotionId);

    List<PromotionDomain> getPromotionByPromotionStatus(Integer Status);

    void updatePromotion(PromotionDomain promotionDomain);
}
