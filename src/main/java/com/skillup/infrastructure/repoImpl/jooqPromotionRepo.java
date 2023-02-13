package com.skillup.infrastructure.repoImpl;

import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionRepository;
import com.skillup.domain.promotion.StockOperation;
import com.skillup.infrastructure.jooq.tables.Promotion;
import com.skillup.infrastructure.jooq.tables.records.PromotionRecord;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "optimistic")
@Slf4j
public class jooqPromotionRepo implements PromotionRepository, StockOperation {
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

    @Override
    public boolean lockStock(String id) {
        /**
         * update promotion
         * set available_stock = available_stock - 1, lock_stock = lock_stock + 1
         * where id = promotion_id and available_stock > 0
         */
        log.info("Starting optimistic-locking...");
        int isLocked = dslContext.update(P_T)
                .set(P_T.AVAILABLE_STOCK, P_T.AVAILABLE_STOCK.subtract(1))
                .set(P_T.LOCK_STOCK, P_T.LOCK_STOCK.add(1))
                .where(P_T.PROMOTION_ID.eq(id).and(P_T.AVAILABLE_STOCK.greaterThan(0L)))
                .execute();
        return isLocked == 1;
    }

    @Override
    public boolean deductStock(String id) {
        /**
         * update promotion
         * set lock_stock = lock_stock - 1
         * where id = promotion_id and lock_stock > 0
         */
        int deducted = dslContext.update(P_T)
                .set(P_T.LOCK_STOCK, P_T.LOCK_STOCK.subtract(1))
                .where(P_T.PROMOTION_ID.eq(id).and(P_T.LOCK_STOCK.greaterThan(0L)))
                .execute();
        return deducted == 1;
    }

    @Override
    public boolean revertStock(String id) {
        /**
         * update promotion
         * set available_stock = available_stock + 1, lock_stock = lock_stock - 1
         * where id = promotion_id and available_stock > 0
         */
        int reverted = dslContext.update(P_T)
                .set(P_T.AVAILABLE_STOCK, P_T.AVAILABLE_STOCK.add(1))
                .set(P_T.LOCK_STOCK, P_T.LOCK_STOCK.subtract(1))
                .where(P_T.PROMOTION_ID.eq(id).and(P_T.AVAILABLE_STOCK.greaterThan(0L)))
                .execute();
        return reverted == 1;
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
