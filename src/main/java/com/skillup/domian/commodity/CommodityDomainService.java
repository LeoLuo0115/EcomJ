package com.skillup.domian.commodity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommodityDomainService {

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
