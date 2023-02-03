package com.skillup.apiPresentation.util;

import com.skillup.apiPresentation.dto.in.PromotionInDto;
import com.skillup.apiPresentation.dto.out.PromotionOutDto;
import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/promotion")

public class PromotionController {
    @Autowired
    PromotionService promotionService;
    @PostMapping
    public ResponseEntity<PromotionOutDto> createPromotion(@RequestBody PromotionInDto promotionInDto) {
        PromotionDomain PromotionDomainSaved  = promotionService.createPromotion(toDomain(promotionInDto));
        return ResponseEntity.status(SkillResponseUtil.SUCCESS).body(toPromotionDomainOutDto(PromotionDomainSaved));
    }

    @GetMapping("/id/{id}")
    public  ResponseEntity<PromotionOutDto> getPromotionById(@PathVariable("id") String id) {
        // hit promotion cache. stock maynot accurate
        PromotionDomain promotionDomain = promotionService.getPromotionById(id);
        if (Objects.isNull(promotionDomain)) {
            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(null);
        }
        return  ResponseEntity.status(SkillResponseUtil.SUCCESS).body(toPromotionDomainOutDto(promotionDomain));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PromotionOutDto>> getPromotionByStatus(@PathVariable("status")Integer status) {
        List<PromotionDomain> promotionDomainList = promotionService.getPromotionByStatus(status);
        return ResponseEntity.status(SkillResponseUtil.SUCCESS).body(
                promotionDomainList.stream().map(this::toPromotionOutDto).collect(collectors.toList())
        );
    }

    private PromotionDomain toDomain(PromotionInDto promotionInDto){
        return PromotionDomain.builder()
                .promotionId(UUID.randomUUID().toString())
                .promotionName(promotionInDto.getPromotionName())
                .commodityId(promotionInDto.getCommodityId())
                .startTime(promotionInDto.getStartTime())
                .endTime(promotionInDto.getEndTime())
                .originalPrice(promotionInDto.getOriginalPrice())
                .promotionPrice(promotionInDto.getPromotionPrice())
                .totalStock(promotionInDto.getTotalStock())
                .avaliableStock(promotionInDto.getAvaliableStock())
                .lockStock(promotionInDto.getLockStock())
                .imageUrl(promotionInDto.getImageUrl())
                .build();
    }

    private PromotionDomain toPromotionDomainOutDto(PromotionOutDto promotionOutDto) {
        return PromotionDomain.builder()
                .promotionId(UUID.randomUUID().toString())
                .promotionName(promotionOutDto.getPromotionName())
                .commodityId(promotionOutDto.getCommodityId())
                .startTime(promotionOutDto.getStartTime())
                .endTime(promotionOutDto.getEndTime())
                .originalPrice(promotionOutDto.getOriginalPrice())
                .promotionPrice(promotionOutDto.getPromotionPrice())
                .totalStock(promotionOutDto.getTotalStock())
                .avaliableStock(promotionOutDto.getAvaliableStock())
                .lockStock(promotionOutDto.getLockStock())
                .imageUrl(promotionOutDto.getImageUrl())
                .build();
    }


}
