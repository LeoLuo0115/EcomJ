package com.skillup.infrastructure.repoImpl;

import com.skillup.domain.commodity.CommodityDomain;
import com.skillup.domain.commodity.CommodityRepository;
import com.skillup.infrastructure.jooq.tables.Commodity;
import com.skillup.infrastructure.jooq.tables.records.CommodityRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JooqCommodityRepo implements CommodityRepository {

    @Autowired
    DSLContext dslContext;

    public static final Commodity COMMODITY_T = new Commodity();

    public void createCommodity(CommodityDomain commodityDomain){

        dslContext.executeInsert(toRecord(commodityDomain));
    }

    public CommodityDomain getCommodityById(String id){
        Optional<CommodityDomain> commodityDomainOptional = dslContext.selectFrom(COMMODITY_T).where(COMMODITY_T.COMMODITY_ID.eq(id)).fetchOptional(this::toDomain);
        return commodityDomainOptional.orElse(null);
    }

    private CommodityRecord toRecord(CommodityDomain cd){
        CommodityRecord cr = new CommodityRecord();
        cr.setCommodityId(cd.getCommodityId());
        cr.setCommodityName(cd.getCommodityName());
        cr.setPrice(cd.getPrice());
        cr.setDescription(cd.getDescription());
        cr.setImageUrl(cd.getImageUrl());
        return cr;
    }

    private CommodityDomain toDomain(CommodityRecord cr){
        return CommodityDomain.builder()
                .commodityId(cr.getCommodityId())
                .commodityName(cr.getCommodityName())
                .price(cr.getPrice())
                .imageUrl(cr.getImageUrl())
                .description(cr.getDescription())
                .build();
    }
}
