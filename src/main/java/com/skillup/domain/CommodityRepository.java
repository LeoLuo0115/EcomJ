package com.skillup.domain;

public interface CommodityRepository {
    void createCommodity(CommodityDomain commodityDomain);
    CommodityDomain getCommodityById(String id);
}
