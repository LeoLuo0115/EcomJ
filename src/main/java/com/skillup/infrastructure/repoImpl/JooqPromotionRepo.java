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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository(value = "optimistic")
@Slf4j
public class JooqPromotionRepo implements PromotionRepository, StockOperation, DomainRecord<PromotionDomain, PromotionRecord>{
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


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean lockStock(String id) {
        /**
         * update promotion
         * set available_stock = available_stock - 1, lock_stock = lock_stock + 1
         * where id = promotion_id and available_stock > 0
         */
        log.info(" start Optimistic-locking...");
        int isLocked = dslContext.update(P_T)
                .set(P_T.AVAILABLE_STOCK, P_T.AVAILABLE_STOCK.subtract(1))
                .set(P_T.LOCK_STOCK, P_T.LOCK_STOCK.add(1))
                .where(P_T.PROMOTION_ID.eq(id).and(P_T.AVAILABLE_STOCK.greaterThan(0L)))
                .execute();
        return isLocked == 1;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
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
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean revertStock(String id) {
        /**
         * update promotion
         * set available_stock = available_stock + 1, lock_stock = lock_stock - 1
         * where id = promotion_id and lock_stock > 0
         */
        int reverted = dslContext.update(P_T)
                .set(P_T.AVAILABLE_STOCK, P_T.AVAILABLE_STOCK.add(1))
                .set(P_T.LOCK_STOCK, P_T.LOCK_STOCK.subtract(1))
                .where(P_T.PROMOTION_ID.eq(id).and(P_T.LOCK_STOCK.greaterThan(0L)))
                .execute();
        return reverted == 1;
    }


    @Override
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

    @Override
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
