package com.skillup.infrastructure.repoImpl;

import com.skillup.domain.CommodityDomain;
import com.skillup.domain.CommodityRepository;
import com.skillup.infrastructure.jooq.tables.records.CommodityRecord;
import com.skillup.infrastructure.jooq.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.jooq.TableRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class JooqCommodityRepo implements CommodityRepository {
    @Autowired // create a DSLContext bean in Spring container
    DSLContext dslContext; // create a thread pool to connect db (provided by JOOQ)


    @Override
    public void createCommodity(CommodityDomain commodityDomain) {
        dslContext.executeInsert(toRecord(commodityDomain));
    }

    @Override
    public CommodityDomain getCommodityById(String id) {
        return null;
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

}
