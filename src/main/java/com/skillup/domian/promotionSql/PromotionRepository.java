package com.skillup.domian.promotionSql;

import java.util.List;

public interface PromotionRepository {
    void createPromotion(PromotionDomain promotionDomain);

    PromotionDomain getPromotionById(String promotionId);

    List<PromotionDomain> getPromotionByStatus(Integer promotionStatus);

    void updatePromotion(PromotionDomain promotionDomain);
}
