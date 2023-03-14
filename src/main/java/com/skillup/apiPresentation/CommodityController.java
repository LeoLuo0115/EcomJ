package com.skillup.apiPresentation;


import com.skillup.apiPresentation.dto.in.CommodityInDto;
import com.skillup.apiPresentation.dto.out.CommodityOutDto;
import com.skillup.apiPresentation.util.ResponseUtil;
import com.skillup.domain.commodity.CommodityDomain;
import com.skillup.domain.commodity.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
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

        if  (Objects.isNull(commodityDomain)) {
            return  ResponseEntity.status(ResponseUtil.BAD_REQUEST).body(null);
        }

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
