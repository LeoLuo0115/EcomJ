package com.skillup.presentation;

import com.skillup.domain.commodity.CommodityDomain;
import com.skillup.domain.commodity.CommodityRepository;
import com.skillup.domain.commodity.CommodityService;
import com.skillup.presentation.dto.in.CommodityInDto;
import com.skillup.presentation.dto.out.CommodityOutDto;
import com.skillup.presentation.util.ResponseCode;
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

    @PostMapping
    public ResponseEntity<CommodityOutDto> createCommodity(@RequestBody CommodityInDto commodityInDto) {
        CommodityDomain commodityDomain = commodityService.registry(toDomain(commodityInDto));
        return ResponseEntity.status(ResponseCode.SUCCESS).body(toOutDto(commodityDomain));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<CommodityOutDto> getCommodityById(@PathVariable("id") String id) {
        CommodityDomain commodityDomain = commodityService.getById(id);
        if(Objects.isNull(commodityDomain)) {
            return ResponseEntity.status(ResponseCode.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(ResponseCode.SUCCESS).body(toOutDto(commodityDomain));
    }

    private CommodityOutDto toOutDto(CommodityDomain commodityDomain) {
        return CommodityOutDto.builder()
                .commodityId(commodityDomain.getCommodityId())
                .commodityName(commodityDomain.getCommodityName())
                .description(commodityDomain.getDescription())
                .price(commodityDomain.getPrice())
                .image_url(commodityDomain.getImage_url())
                .build();
    }

    private CommodityDomain toDomain(CommodityInDto commodityInDto) {
        return CommodityDomain.builder()
                .commodityId(UUID.randomUUID().toString())
                .commodityName(commodityInDto.getCommodityName())
                .description(commodityInDto.getDescription())
                .price(commodityInDto.getPrice())
                .image_url(commodityInDto.getImage_url())
                .build();
    }

}
