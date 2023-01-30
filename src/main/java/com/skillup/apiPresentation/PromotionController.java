package com.skillup.apiPresentation;

import com.skillup.apiPresentation.dto.in.PromotionInDto;
import com.skillup.apiPresentation.dto.out.PromotionOutDto;
import com.skillup.apiPresentation.util.SkillResponseUtil;
import com.skillup.apiPresentation.util.SkillUpResponse;
import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/promotion")
public class PromotionController {
    @Autowired
    PromotionDomainService promotionDomainService;

    @PostMapping
    public ResponseEntity<SkillUpResponse> createPromotion(@RequestBody PromotionInDto promotionInDto){
        PromotionDomain promotionDomain = toDomain(promotionInDto);
        System.out.println(promotionDomain.getPromotionName());
        System.out.println("atcontroller");
        PromotionDomain promotion = null;
        try {
            promotion = promotionDomainService.createPromotion(promotionDomain);
        } catch (Exception e) {

            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(SkillResponseUtil.PROMO_ALREADY_EXISTS).build());
        }
        return ResponseEntity.status(SkillResponseUtil.SUCCESS).body(SkillUpResponse.builder().result(toOutDto(promotion)).build());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<SkillUpResponse> getPromotionById(@PathVariable("id") String id){
        PromotionDomain promotionById = promotionDomainService.getPromotionById(id);
        if(Objects.isNull(promotionById)){
            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(SkillResponseUtil.NO_SUCH_PROMO_ID).build());

        }
        return ResponseEntity.status(SkillResponseUtil.SUCCESS).body(SkillUpResponse.builder().result(toOutDto(promotionById)).build());

    }

    @GetMapping("/status/{status}")
    public ResponseEntity<SkillUpResponse> getPromotionByStatus(@PathVariable("status") Integer status){

        if(status==1 || status==2 || status==0){
            System.out.println(status + "inside the if");
            List<PromotionDomain> promotionDomainList = promotionDomainService.getPromotionByStatus(status);
            return ResponseEntity.status(SkillResponseUtil.SUCCESS).body(SkillUpResponse.builder().result(promotionDomainList).build());
        }
        return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().build());
    }

    @PostMapping("/lock/id/{id}")
    public ResponseEntity<Boolean> lockStock(@PathVariable("id") String id){
        PromotionDomain promotionById = promotionDomainService.getPromotionById(id);
        if (Objects.isNull(promotionById)) {
            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(false);
        }
        System.out.println("right before controller cal");
        boolean isLocked = promotionDomainService.lockstock(id);
        return ResponseEntity.status(SkillResponseUtil.SUCCESS).body(isLocked);
    }

    @PostMapping("/deduct/id/{id}")
    public ResponseEntity<Boolean> deductStock(@PathVariable("id") String id){
        PromotionDomain promotionById = promotionDomainService.getPromotionById(id);
        if (Objects.isNull(promotionById)) {
            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(false);

        }
        boolean isDeducted = promotionDomainService.deductStock(id);
        return ResponseEntity.status(SkillResponseUtil.SUCCESS).body(isDeducted);
    }

    @PostMapping("/revert/id/{id}")
    public ResponseEntity<Boolean> revertStock(@PathVariable("id") String id){
        PromotionDomain promotionById = promotionDomainService.getPromotionById(id);
        if (Objects.isNull(promotionById)) {
            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(false);

        }
        boolean isReverted = promotionDomainService.revertStock(id);
        return ResponseEntity.status(SkillResponseUtil.SUCCESS).body(isReverted);
    }




    private PromotionDomain toDomain(PromotionInDto promotionInDto){
        return PromotionDomain.builder()
                .promotionId(UUID.randomUUID().toString())
                .availableStock(promotionInDto.getAvailableStock())
                .commodityId(promotionInDto.getCommodityId())
                .promotionalPrice(promotionInDto.getPromotionalPrice())
                .promotionName(promotionInDto.getPromotionName())
                .status(promotionInDto.getStatus())
                .imageUrl(promotionInDto.getImageUrl())
                .endTime(promotionInDto.getEndTime())
                .startTime(promotionInDto.getStartTime())
                .lockStock(promotionInDto.getLockStock())
                .totalStock(promotionInDto.getTotalStock())
                .originalPrice(promotionInDto.getOriginalPrice())
                .build();
    }

    private PromotionOutDto toOutDto(PromotionDomain promotionDomain){
        return PromotionOutDto.builder()
                .promotionId(promotionDomain.getPromotionId())
                .availableStock(promotionDomain.getAvailableStock())
                .commodityId(promotionDomain.getCommodityId())
                .promotionalPrice(promotionDomain.getPromotionalPrice())
                .promotionName(promotionDomain.getPromotionName())
                .status(promotionDomain.getStatus())
                .imageUrl(promotionDomain.getImageUrl())
                .endTime(promotionDomain.getEndTime())
                .startTime(promotionDomain.getStartTime())
                .lockStock(promotionDomain.getLockStock())
                .totalStock(promotionDomain.getTotalStock())
                .originalPrice(promotionDomain.getOriginalPrice())
                .build();
    }
}
