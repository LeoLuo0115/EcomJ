package com.skillup.infrastructure.repoImpl;

import com.skillup.domain.CommodityDomain;
import com.skillup.domain.CommodityRepository;
import com.skillup.infrastructure.jooq.tables.Commodity;
import com.skillup.infrastructure.jooq.tables.User;
import com.skillup.infrastructure.jooq.tables.records.CommodityRecord;
import com.skillup.infrastructure.jooq.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.jooq.TableRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JooqCommodityRepo implements CommodityRepository {
    @Autowired // create a DSLContext bean in Spring container
    DSLContext dslContext; // create a thread pool to connect db (provided by JOOQ)

    // generate the COMMODITY table only once, for getter methods
    public static final Commodity COMMODITY_T = new Commodity();

    @Override
    public void createCommodity(CommodityDomain commodityDomain) {
        dslContext.executeInsert(toRecord(commodityDomain));
    }

    @Override
    public CommodityDomain getCommodityById(String id) {
        Optional<CommodityDomain> commodityDomainOptional = dslContext
                .selectFrom(COMMODITY_T).where(COMMODITY_T.COMMODITY_ID.eq(id))
                .fetchOptional(this::toDomain);
        return commodityDomainOptional.orElse(null);
    }


    private CommodityRecord toRecord(CommodityDomain commodityDomain) {
        CommodityRecord commodityRecord = new CommodityRecord();
        commodityRecord.setCommodityId(commodityDomain.getCommodityId());
        commodityRecord.setCommodityName(commodityDomain.getCommodityName());
        commodityRecord.setDescription(commodityDomain.getDescription());
        commodityRecord.setPrice(commodityDomain.getPrice());
        commodityRecord.setImageUrl(commodityDomain.getImageUrl());
        return commodityRecord;
    }

    private CommodityDomain toDomain(CommodityRecord commodityRecord) {
        return CommodityDomain.builder()
                .commodityId(commodityRecord.getCommodityId())
                .commodityName(commodityRecord.getCommodityName())
                .description(commodityRecord.getDescription())
                .price(commodityRecord.getPrice())
                .imageUrl(commodityRecord.getImageUrl())
                .build();
    }
}
