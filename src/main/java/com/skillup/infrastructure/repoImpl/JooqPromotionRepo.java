package com.skillup.infrastructure.repoImpl;

import com.skillup.domain.commodity.CommodityDomain;
import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionRepository;
import com.skillup.domain.promotion.StockOperation;
import com.skillup.infrastructure.jooq.tables.Promotion;
import com.skillup.infrastructure.jooq.tables.records.PromotionRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository(value="optimistic")
public class JooqPromotionRepo implements PromotionRepository, StockOperation {

    @Autowired
    DSLContext dslContext;

    public static final Promotion PROMOTION_T = new Promotion();

    public void createPromotion(PromotionDomain promotionDomain) {
        System.out.println("atJooq");
        PromotionRecord promotionRecord = toRecord(promotionDomain);
        System.out.println(promotionRecord.getPromotionName());
        dslContext.executeInsert(promotionRecord);
    }


    public PromotionDomain getPromotionById(String id) {
        Optional<PromotionDomain> promotionDomainOptional = dslContext.selectFrom(PROMOTION_T).where(PROMOTION_T.PROMOTION_ID.eq(id)).fetchOptional(this::toDomain);

        return promotionDomainOptional.orElse(null);

    }

    @Override
    public List<PromotionDomain> getPromotionByStatus(Integer status) {
        return dslContext.selectFrom(PROMOTION_T).where(PROMOTION_T.STATUS.eq(status)).fetch(this::toDomain);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean lockStock(String id){
        int isLocked = dslContext.update(PROMOTION_T)
                .set(PROMOTION_T.AVAILABLE_STOCK, PROMOTION_T.AVAILABLE_STOCK.subtract(1))
                .set(PROMOTION_T.LOCK_STOCK, PROMOTION_T.LOCK_STOCK.add(1))
                .where(PROMOTION_T.PROMOTION_ID.eq(id).and(PROMOTION_T.AVAILABLE_STOCK.greaterThan(0L)))
                .execute();
        return isLocked == 1;
    }

    @Override
    public boolean deductStock(String id) {
        int deducted = dslContext.update(PROMOTION_T)
                .set(PROMOTION_T.LOCK_STOCK, PROMOTION_T.LOCK_STOCK.subtract(1))
                .where(PROMOTION_T.PROMOTION_ID.eq(id).and(PROMOTION_T.LOCK_STOCK.greaterThan(0L)))
                .execute();
        return deducted == 1;
    }

    @Override
    public boolean revertStock(String id) {


        int reverted = dslContext.update(PROMOTION_T)
                .set(PROMOTION_T.AVAILABLE_STOCK, PROMOTION_T.AVAILABLE_STOCK.add(1))
                .set(PROMOTION_T.LOCK_STOCK, PROMOTION_T.LOCK_STOCK.subtract(1))
                .where(PROMOTION_T.PROMOTION_ID.eq(id).and(PROMOTION_T.LOCK_STOCK.greaterThan(0L)))
                .execute();
        return reverted == 1;
    }


    public void updatePromotion(PromotionDomain promotionDomain){
        dslContext.executeUpdate(toRecord(promotionDomain));
    }

    private PromotionRecord toRecord(PromotionDomain promotionDomain){
        PromotionRecord pr = new PromotionRecord();
        pr.setPromotionId(promotionDomain.getPromotionId());
        pr.setCommodityId(promotionDomain.getCommodityId());
        pr.setImageUrl(promotionDomain.getImageUrl());
        pr.setAvailableStock(promotionDomain.getAvailableStock());
        pr.setEndTime(promotionDomain.getEndTime());
        pr.setLockStock(promotionDomain.getLockStock());
        pr.setOriginalPrice(promotionDomain.getOriginalPrice());
        pr.setPromotionName(promotionDomain.getPromotionName());
        pr.setStatus(promotionDomain.getStatus());
        pr.setTotalStock(promotionDomain.getTotalStock());
        pr.setLockStock(promotionDomain.getLockStock());
        pr.setStartTime(promotionDomain.getStartTime());
        pr.setPromotionPrice(promotionDomain.getPromotionalPrice());
        return pr;
    }

    private PromotionDomain toDomain(PromotionRecord promotionRecord){
        PromotionDomain pr = new PromotionDomain();
        pr.setPromotionId(promotionRecord.getPromotionId());
        pr.setCommodityId(promotionRecord.getCommodityId());
        pr.setImageUrl(promotionRecord.getImageUrl());
        pr.setAvailableStock(promotionRecord.getAvailableStock());
        pr.setEndTime(promotionRecord.getEndTime());
        pr.setLockStock(promotionRecord.getLockStock());
        pr.setOriginalPrice(promotionRecord.getOriginalPrice());
        pr.setPromotionName(promotionRecord.getPromotionName());
        pr.setStatus(promotionRecord.getStatus());
        pr.setTotalStock(promotionRecord.getTotalStock());
        pr.setLockStock(promotionRecord.getLockStock());
        pr.setStartTime(promotionRecord.getStartTime());
        pr.setPromotionalPrice(promotionRecord.getPromotionPrice());
        return pr;
    }

}
