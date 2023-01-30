package com.skillup.APIpresentation;


import com.skillup.APIpresentation.dto.in.CommodityInDto;
import com.skillup.APIpresentation.dto.out.CommodityOutDto;
import com.skillup.APIpresentation.util.ResponseUtil;
import com.skillup.domian.commodity.CommodityDomain;
import com.skillup.domian.commodity.CommodityDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/commodity")
public class CommodityController {

    @Autowired
    CommodityDomainService commodityDomainService;

    // Create Commodity API
    @PostMapping
    public ResponseEntity<CommodityOutDto> createCommodity(@RequestBody CommodityInDto commodityInDto) {

        CommodityDomain savedCommodityDomain = commodityDomainService.registry(toDomain(commodityInDto));


        return ResponseEntity.status(ResponseUtil.SUCCESS).body(toOutDto(savedCommodityDomain));
    }

    private CommodityDomain toDomain(CommodityInDto commodityInDto) {
        return CommodityDomain.builder()
                .commodityId(UUID.randomUUID().toString())
                .commodityName(commodityInDto.getCommodityName())
                .description(commodityInDto.getDescription())
                .price(commodityInDto.getPrice())
                .imageUrl(commodityInDto.getImageUrl())
                .build();
    }

    private CommodityOutDto toOutDto(CommodityDomain commodityDomain) {
        return CommodityOutDto.builder()
                .commodityId(commodityDomain.getCommodityId())
                .commodityName(commodityDomain.getCommodityName())
                .description(commodityDomain.getDescription())
                .price(commodityDomain.getPrice())
                .imageUrl(commodityDomain.getImageUrl())
                .build();

    }
}
