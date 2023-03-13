package com.skillup.domain.commodity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommodityService {

    @Autowired
    CommodityRepository CommodityRepository;

    public CommodityDomain registry(CommodityDomain commodityDomain) {
        CommodityRepository.createCommodity(commodityDomain);
        return commodityDomain;
    }

    public CommodityDomain getCommodityById(String commodityId) {
        return CommodityRepository.getCommodityById(commodityId);
    }

}
