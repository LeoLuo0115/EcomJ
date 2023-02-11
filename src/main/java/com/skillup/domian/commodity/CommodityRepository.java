package com.skillup.domian.commodity;

public interface CommodityRepository {
    void createCommodity(CommodityDomain commodityDomain);

    CommodityDomain getCommodityById(String id);

}
