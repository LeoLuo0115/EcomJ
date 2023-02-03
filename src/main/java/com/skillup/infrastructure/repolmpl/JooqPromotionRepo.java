package com.skillup.infrastructure.repolmpl;

import com.skillup.apiPresentation.dto.in.PromotionInDto;
import com.skillup.apiPresentation.dto.out.PromotionOutDto;
import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionRepository;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class JooqPromotionRepo implements PromotionRepository{

    @Autowired
    DSLContext dslContext;

    public static final Promotion P_T = new Promotion();
    @Override
    public void createPromotion(PromotionDomain promotionDomain) {
        dslContext.executeInsert(toRecord(promotionDomain));
    }

    @Override
    public PromotionDomain getPromotionByPromotionId(String promotionId) {
        return null;
    }

    @Override
    public List<PromotionDomain> getPromotionByPromotionStatus(Integer Status) {
        return null;
    }

    @Override
    public void updatePromotion(PromotionDomain promotionDomain) {

    }

    public PromotionDomain toDomain(PromotionRecord record){
        return PromotionDomain.builder()
                .promotionId(reocrd.getPromitionId())

                .build();
    }

    public PromotionRecord toRecord(PromotionDomain domain) {
            return new PromotionRecord(
                    domain.getPromotionId(),
                    domain.getPromotionName(),
                    domain.getCommodityId(),
                    domain.getOriginalPrice(),
                    domain.getPromotionPrice(),
                    domain.getStartTime(),
                    domain.getEndTime(),
                    domain.getStatus(),
                    domain.getTotalStock(),
                    domain.getAvaliableStock(),
                    domain.getLockStock(),
                    domain.getImageUrl()
            );
        }

}
