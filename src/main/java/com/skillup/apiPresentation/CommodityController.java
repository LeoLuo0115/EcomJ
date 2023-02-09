package com.skillup.apiPresentation;

import com.skillup.apiPresentation.dto.in.CommodityInDto;
import com.skillup.apiPresentation.dto.in.UserInDto;
import com.skillup.apiPresentation.dto.out.CommodityOutDto;
import com.skillup.apiPresentation.util.SkillUpResponse;
import com.skillup.apiPresentation.util.SkillUpResponseUtil;
import com.skillup.domain.CommodityDomain;
import com.skillup.domain.CommodityDomainService;
import com.skillup.domain.UserDomain;
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
    public ResponseEntity<CommodityOutDto> createUser(@RequestBody CommodityInDto commodityInDto){

        // create commodityDomain
        CommodityDomain commodityDomain = toDomain(commodityInDto);
        // call domain service to create commodity
        CommodityDomain savedCommodityDomain = commodityDomainService.createCommodity(commodityDomain);

        return ResponseEntity.status(SkillUpResponseUtil.SUCCESS).body(
                toOutDto(savedCommodityDomain));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<CommodityOutDto> getCommodityById(@PathVariable("id") String commodityId){
        CommodityDomain commodityDomain = commodityDomainService.getCommodityById(commodityId);
        // commodity doesn't exist
        if (Objects.isNull(commodityDomain)){
            return ResponseEntity.status(SkillUpResponseUtil.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(SkillUpResponseUtil.SUCCESS).body(
                toOutDto(commodityDomain)
        );
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
