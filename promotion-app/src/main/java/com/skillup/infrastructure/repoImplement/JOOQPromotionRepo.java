package com.skillup.infrastructure.repoImplement;


import com.skillup.domain.promotionSql.PromotionDomain;
import com.skillup.domain.promotionSql.PromotionRepository;
import com.skillup.domain.promotionSql.StockOperation;
import com.skillup.infrastructure.jooq.tables.Promotion;
import com.skillup.infrastructure.jooq.tables.records.PromotionRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository(value = "optimistic")
public class JOOQPromotionRepo implements PromotionRepository, StockOperation {
    @Autowired
    DSLContext dslContext;

    public static final Promotion Promotion_T = new Promotion();


    @Override
    public void createPromotion(PromotionDomain promotionDomain) {
        dslContext.executeInsert(toRecord(promotionDomain));
    }

    public PromotionDomain getPromotionById(String promotionId) {
        Optional<PromotionDomain> PromotionDomainOptional = dslContext.selectFrom(Promotion_T)
                .where(Promotion_T.PROMOTION_ID.eq(promotionId)).fetchOptional(this::toDomain);
        return PromotionDomainOptional.orElse(null);
    }

    @Override
    public List<PromotionDomain> getPromotionByStatus(Integer promotionStatus) {
        return dslContext.selectFrom(Promotion_T).where(Promotion_T.STATUS.eq(promotionStatus)).fetch(this::toDomain);
    }

    @Override
    public void updatePromotion(PromotionDomain promotionDomain) {
        dslContext.executeUpdate(toRecord(promotionDomain));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean lockStock(String id) {
        /**
         * update promotion
         * set available_stock = available_stock - 1, lock_stock = lock_stock + 1
         * where id = promotion_id and available_stock > 0
         */
        // log.info(" start Optimistic-locking...");
        int isLocked = dslContext.update(Promotion_T)
                .set(Promotion_T.AVAILABLE_STOCK, Promotion_T.AVAILABLE_STOCK.subtract(1))
                .set(Promotion_T.LOCK_STOCK, Promotion_T.LOCK_STOCK.add(1))
                .where(Promotion_T.PROMOTION_ID.eq(id).and(Promotion_T.AVAILABLE_STOCK.greaterThan(0L)))
                .execute();
        return isLocked == 1;
    }

    // 扣除库存，规定时间下单成功, 不需要触及 available stock
    @Override
    public boolean deductStock(String id) {
        /**
         * update promotion
         * set lock_stock = lock_stock - 1
         * where id = promotion_id and lock_stock > 0
         */
        int deducted = dslContext.update(Promotion_T)
                .set(Promotion_T.LOCK_STOCK, Promotion_T.LOCK_STOCK.subtract(1))
                .where(Promotion_T.PROMOTION_ID.eq(id).and(Promotion_T.LOCK_STOCK.greaterThan(0L)))
                .execute();
        return deducted == 1;
    }

    // 归还库存，规定时间没有下单成功
    @Override
    public boolean revertStock(String id) {
        /**
         * update promotion
         * set available_stock = available_stock + 1, lock_stock = lock_stock - 1
         * where id = promotion_id and lock_stock > 0
         */
        int reverted = dslContext.update(Promotion_T)
                .set(Promotion_T.AVAILABLE_STOCK, Promotion_T.AVAILABLE_STOCK.add(1))
                .set(Promotion_T.LOCK_STOCK, Promotion_T.LOCK_STOCK.subtract(1))
                .where(Promotion_T.PROMOTION_ID.eq(id).and(Promotion_T.LOCK_STOCK.greaterThan(0L)))
                .execute();
        return reverted == 1;
    }


    private PromotionRecord toRecord(PromotionDomain promotionDomain) {
        return new PromotionRecord(
                promotionDomain.getPromotionId(),
                promotionDomain.getPromotionName(),
                promotionDomain.getCommodityId(),
                promotionDomain.getOriginalPrice(),
                promotionDomain.getPromotionalPrice(),
                promotionDomain.getStartTime(),
                promotionDomain.getEndTime(),
                promotionDomain.getStatus(),
                promotionDomain.getTotalStock(),
                promotionDomain.getAvailableStock(),
                promotionDomain.getLockStock(),
                promotionDomain.getImageUrl()
        );
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
}
