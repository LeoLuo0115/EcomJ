package com.skillup.apiPresentation;

import com.skillup.apiPresentation.dto.in.CommodityInDto;
import com.skillup.apiPresentation.dto.out.CommodityOutDto;
import com.skillup.apiPresentation.util.SkillResponseUtil;
import com.skillup.apiPresentation.util.SkillUpResponse;
import com.skillup.domain.commodity.CommodityDomain;
import com.skillup.domain.commodity.CommodityDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/commodity")
public class CommodityController {

    @Autowired
    CommodityDomainService commodityDomainService;

    @PostMapping
    public ResponseEntity<SkillUpResponse> createCommodity(@RequestBody CommodityInDto commodityInDto){

        CommodityDomain commodityDomain = toDomain(commodityInDto);
        System.out.println(commodityDomain.getCommodityName());
        System.out.println(commodityDomain.getPrice());
        System.out.println(commodityDomain.getImageUrl());
        System.out.println(commodityDomain.getDescription());

        CommodityDomain commodity= null;
        try {
            commodity = commodityDomainService.createCommodity(commodityDomain);
        } catch (Exception e) {
            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(SkillResponseUtil.COM_EXISTS).build());
        }
        return ResponseEntity.status(SkillResponseUtil.SUCCESS).body(SkillUpResponse.builder().result(toOutDto(commodity)).build());

    }

    @GetMapping("/id/{id}")
    public ResponseEntity<SkillUpResponse> getCommodityById(@PathVariable("id") String id){
        CommodityDomain commodityById = commodityDomainService.getCommodityById(id);
        if (Objects.isNull(commodityById)) {
            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(SkillResponseUtil.COM_NOT_EXISTS).build());
        }
        return ResponseEntity.status(SkillResponseUtil.SUCCESS).body(SkillUpResponse.builder().result(toOutDto(commodityById)).build());

    }


    private CommodityDomain toDomain(CommodityInDto commodityInDto){
        return CommodityDomain.builder()
                .commodityId(UUID.randomUUID().toString())
                .commodityName(commodityInDto.getCommodityName())
                .description(commodityInDto.getDescription())
                .price(commodityInDto.getPrice())
                .imageUrl(commodityInDto.getImageUrl())
                .build();
    }

    private CommodityOutDto toOutDto(CommodityDomain commodityDomain){
        return CommodityOutDto.builder()
                .commodityId(commodityDomain.getCommodityId())
                .commodityName(commodityDomain.getCommodityName())
                .price(commodityDomain.getPrice())
                .description(commodityDomain.getDescription())
                .imageUrl(commodityDomain.getImageUrl())
                .build();
    }

}
