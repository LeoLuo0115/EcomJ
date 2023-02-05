package com.skillup.APIpresentation;


import com.skillup.APIpresentation.dto.in.CommodityInDto;
import com.skillup.APIpresentation.dto.out.CommodityOutDto;
import com.skillup.APIpresentation.util.ResponseUtil;
import com.skillup.domian.commodity.CommodityDomain;
import com.skillup.domian.commodity.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/commodity")
public class CommodityController {

    @Autowired
    CommodityService commodityService;

    // Create Commodity API
    // There is no logic to catch errors, return error msg
    // this can be improved by looking at how userDomainService is implemented.
    @PostMapping
    public ResponseEntity<CommodityOutDto> createCommodity(@RequestBody CommodityInDto commodityInDto) {

        CommodityDomain savedCommodityDomain = commodityService.registry(toDomain(commodityInDto));


        return ResponseEntity.status(ResponseUtil.SUCCESS).body(toOutDto(savedCommodityDomain));
    }


    @GetMapping("/id/{id}")
    public ResponseEntity<CommodityOutDto> getCommodity(@PathVariable("id") String commodityId ) {

        CommodityDomain commodityDomain = commodityService.getCommodityById(commodityId);

//        if(commodityDomain.equals(null)) {
//
//        }

        return ResponseEntity.status(ResponseUtil.SUCCESS).body(toOutDto(commodityDomain));
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
