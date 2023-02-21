package com.skillup.domain.commodity;

import com.skillup.infrastructure.jooq.tables.records.CommodityRecord;

public interface CommodityRepository {
    void createCommodity(CommodityDomain commodityDomain);

    CommodityDomain getCommodityById(String id);
}
