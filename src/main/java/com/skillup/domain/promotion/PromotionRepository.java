package com.skillup.domain.promotion;

import com.skillup.infrastructure.jooq.tables.records.PromotionRecord;

import java.util.List;
public interface PromotionRepository {

    void createPromotion(PromotionDomain promotionDomain);

    PromotionDomain getPromotionByPromotionId(String promotionId);

    List<PromotionDomain> getPromotionByPromotionStatus(Integer Status);

    void updatePromotion(PromotionDomain promotionDomain);

    PromotionDomain toDomain(PromotionRecord record);

    PromotionRecord toRecord(PromotionDomain domain);
}
