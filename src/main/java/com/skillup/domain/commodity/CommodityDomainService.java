package com.skillup.domain.commodity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommodityDomainService {

    @Autowired
    CommodityRepository commodityRepository;

    public CommodityDomain createCommodity(CommodityDomain commodityDomain){
        System.out.println("at service");
        commodityRepository.createCommodity(commodityDomain);
        return commodityDomain;
    }

    public CommodityDomain getCommodityById(String id){
        CommodityDomain commodityById = commodityRepository.getCommodityById(id);
        return commodityById;
    }


}
