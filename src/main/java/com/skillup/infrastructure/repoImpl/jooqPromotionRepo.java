package com.skillup.infrastructure.repoImpl;

import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionRepository;
import com.skillup.infrastructure.jooq.tables.Promotion;
import com.skillup.infrastructure.jooq.tables.records.PromotionRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class jooqPromotionRepo implements PromotionRepository {
    @Autowired
    DSLContext dslContext;

    public static final Promotion P_T = new Promotion();

    @Override
    public void createPromotion(PromotionDomain promotionDomain) {
        dslContext.executeInsert(toRecord(promotionDomain));
    }

    @Override
    public PromotionDomain getPromotionById(String id) {
        return dslContext.selectFrom(P_T).where(P_T.PROMOTION_ID.eq(id)).fetchOptional(this::toDomain).orElse(null);
    }

    @Override
    public List<PromotionDomain> getPromotionByStatus(Integer status) {
        return dslContext.selectFrom(P_T).where(P_T.STATUS.eq(status)).fetch(this::toDomain);
    }

    @Override
    public void updatePromotion(PromotionDomain promotionDomain) {
        dslContext.executeUpdate(toRecord(promotionDomain));
    }

    private PromotionDomain toDomain(PromotionRecord promotionRecord) {
        return PromotionDomain.builder()
                .promotionId(promotionRecord.getPromotionId())
                .promotionName(promotionRecord.getPromotionName())
                .commodityId(promotionRecord.getCommodityId())
                .originalPrice(promotionRecord.getOriginalPrice())
                .promotionalPrice(promotionRecord.getPromotionPrice())
                .startTime(promotionRecord.getStartTime())
                .endTime(promotionRecord.getEndTime())
                .status(promotionRecord.getStatus())
                .totalStock(promotionRecord.getTotalStock())
                .availableStock(promotionRecord.getAvailableStock())
                .lockStock(promotionRecord.getLockStock())
                .imageUrl(promotionRecord.getImageUrl())
                .build();
    }

    private PromotionRecord toRecord(PromotionDomain promotionDomain) {
        PromotionRecord promotionRecord = new PromotionRecord();
        promotionRecord.setPromotionId(promotionDomain.getPromotionId());
        promotionRecord.setPromotionName(promotionDomain.getPromotionName());
        promotionRecord.setCommodityId(promotionDomain.getPromotionId());
        promotionRecord.setOriginalPrice(promotionDomain.getOriginalPrice());
        promotionRecord.setPromotionPrice(promotionDomain.getPromotionalPrice());
        promotionRecord.setStatus(promotionDomain.getStatus());
        promotionRecord.setStartTime(promotionDomain.getStartTime());
        promotionRecord.setEndTime(promotionDomain.getEndTime());
        promotionRecord.setTotalStock(promotionDomain.getTotalStock());
        promotionRecord.setAvailableStock(promotionDomain.getAvailableStock());
        promotionRecord.setLockStock(promotionDomain.getLockStock());
        promotionRecord.setImageUrl(promotionDomain.getImageUrl());
        return promotionRecord;
    }
}
