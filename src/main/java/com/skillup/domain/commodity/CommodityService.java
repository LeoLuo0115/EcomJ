package com.skillup.domain.commodity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommodityService {

    @Autowired
    CommodityRepository commodityRepository;
    public CommodityDomain registry(CommodityDomain commodityDomain) {
        commodityRepository.createCommodity(commodityDomain);
        return commodityDomain;
    }

    public CommodityDomain getById(String id) {
        return commodityRepository.getCommodityById(id);
    }
}
