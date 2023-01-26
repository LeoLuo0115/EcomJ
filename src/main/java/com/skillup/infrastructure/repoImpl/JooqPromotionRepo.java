package com.skillup.infrastructure.repoImpl;

import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionRepository;
import com.skillup.infrastructure.jooq.tables.Promotion;
import com.skillup.infrastructure.jooq.tables.User;
import com.skillup.infrastructure.jooq.tables.records.PromotionRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JooqPromotionRepo implements PromotionRepository {
    @Autowired
    DSLContext dslContext;

    public static final Promotion P_T = new Promotion();
    @Override
    public void createPromotion(PromotionDomain promotionDomain) {
        dslContext.executeInsert(toRecord(promotionDomain));
    }

    @Override
    public PromotionDomain getPromotionByPromotionId(String promotionId) {
        return dslContext.selectFrom(P_T).where(P_T.PROMOTION_ID.eq(promotionId)).fetchOptional(this::toDomain).orElse(null);

    }

    @Override
    public List<PromotionDomain> getPromotionByPromotionStatus(Integer status) {
        return dslContext.selectFrom(P_T).where(P_T.STATUS.eq(status)).fetch(this::toDomain);

    }

    @Override
    public void updatePromotion(PromotionDomain promotionDomain) {
        dslContext.executeUpdate(toRecord(promotionDomain));
    }

    public PromotionDomain toDomain(PromotionRecord record) {
        return PromotionDomain.builder()
                .promotionId(record.getPromotionId())
                .promotionName(record.getPromotionName())
                .commodityId(record.getCommodityId())
                .originalPrice(record.getOriginalPrice())
                .promotionalPrice(record.getPromotionPrice())
                .startTime(record.getStartTime())
                .endTime(record.getEndTime())
                .status(record.getStatus())
                .totalStock(record.getTotalStock())
                .availableStock(record.getAvailableStock())
                .lockStock(record.getLockStock())
                .imageUrl(record.getImageUrl())
                .build();
    }

    public PromotionRecord toRecord(PromotionDomain domain) {
        return new PromotionRecord(
                domain.getPromotionId(),
                domain.getPromotionName(),
                domain.getCommodityId(),
                domain.getOriginalPrice(),
                domain.getPromotionalPrice(),
                domain.getStartTime(),
                domain.getEndTime(),
                domain.getStatus(),
                domain.getTotalStock(),
                domain.getAvailableStock(),
                domain.getLockStock(),
                domain.getImageUrl()
        );
    }
}
