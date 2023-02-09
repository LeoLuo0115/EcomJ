package com.skillup.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommodityDomainService {
    @Autowired
    CommodityRepository commodityRepository;

    public CommodityDomain createCommodity(CommodityDomain commodityDomain) {
        commodityRepository.createCommodity(commodityDomain);
        return commodityDomain;
    }
}
