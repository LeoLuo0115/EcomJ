package com.skillup.presentation;

import com.skillup.application.promotion.PromotionApplication;
import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionService;
import com.skillup.domain.stockCache.StockCacheService;
import com.skillup.presentation.dto.in.PromotionInDto;
import com.skillup.presentation.dto.out.PromotionOutDto;
import com.skillup.presentation.util.ResponseCode;
import org.apache.el.parser.BooleanNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/promotion")
public class PromotionController {
    @Autowired
    PromotionService promotionService;

    @Autowired
    PromotionApplication promotionApplication;

    @Autowired
    StockCacheService stockCacheService;

    @PostMapping
    public ResponseEntity<PromotionOutDto> createPromotion(@RequestBody PromotionInDto promotionInDto) {
        PromotionDomain promotionDomain = promotionService.registry(toDomain(promotionInDto));
        return ResponseEntity.status(ResponseCode.SUCCESS).body(toOutDto(promotionDomain));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<PromotionOutDto> getPromotionById(@PathVariable("id") String id) {
        PromotionDomain promotionDomain = promotionApplication.getPromotionById(id);
        if (Objects.isNull(promotionDomain)) {
            return ResponseEntity.status(ResponseCode.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(ResponseCode.SUCCESS).body(toOutDto(promotionDomain));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PromotionOutDto>> getPromotionByStatus(@PathVariable("status") Integer status) {
        List<PromotionDomain> promotionDomainList = promotionService.getPromotionByStatus(status);
        return ResponseEntity.status(ResponseCode.SUCCESS).body(
                promotionDomainList.stream().map(this::toOutDto).collect(Collectors.toList())
        );
    }

    @PostMapping("/lock/id/{id}")
    public ResponseEntity<Boolean> lockPromotionStock(@PathVariable("id") String id) {
        PromotionDomain promotionDomain = promotionApplication.getPromotionById(id);
        if (Objects.isNull(promotionDomain)) {
            return ResponseEntity.status(ResponseCode.BAD_REQUEST).body(false);
        }
        boolean isLocked = stockCacheService.lockStock(id);
        return ResponseEntity.status(ResponseCode.SUCCESS).body(isLocked);
    }

    @PostMapping("/deduct/id/{id}")
    public ResponseEntity<Boolean> deductPromotionStock(@PathVariable("id") String id) {
        PromotionDomain promotionDomain = promotionService.getPromotionById(id);
        if (Objects.isNull(promotionDomain)) {
            return ResponseEntity.status(ResponseCode.BAD_REQUEST).body(false);
        }
        boolean isDeducted = promotionService.deductStock(id);
        return ResponseEntity.status(ResponseCode.SUCCESS).body(isDeducted);
    }

    @PostMapping("/revert/id/{id}")
    public ResponseEntity<Boolean> revertPromotionStock(@PathVariable("id") String id) {
        PromotionDomain promotionDomain = promotionApplication.getPromotionById(id);
        if (Objects.isNull(promotionDomain)) {
            return ResponseEntity.status(ResponseCode.BAD_REQUEST).body(false);
        }
        boolean isReverted = stockCacheService.revertStock(id);
        return ResponseEntity.status(ResponseCode.SUCCESS).body(isReverted);
    }



    private PromotionOutDto toOutDto(PromotionDomain promotionDomain) {
        return PromotionOutDto.builder()
                .promotionId(promotionDomain.getPromotionId())
                .promotionName(promotionDomain.getPromotionName())
                .commodityId(promotionDomain.getCommodityId())
                .startTime(promotionDomain.getStartTime())
                .endTime(promotionDomain.getEndTime())
                .totalStock(promotionDomain.getTotalStock())
                .availableStock(promotionDomain.getAvailableStock())
                .lockStock(promotionDomain.getLockStock())
                .originalPrice(promotionDomain.getOriginalPrice())
                .promotionalPrice(promotionDomain.getPromotionalPrice())
                .imageUrl(promotionDomain.getImageUrl())
                .status(promotionDomain.getStatus())
                .build();
    }

    private PromotionDomain toDomain(PromotionInDto promotionInDto) {
        return PromotionDomain.builder()
                .promotionId(UUID.randomUUID().toString())
                .promotionName(promotionInDto.getPromotionName())
                .commodityId(promotionInDto.getCommodityId())
                .originalPrice(promotionInDto.getOriginalPrice())
                .promotionalPrice(promotionInDto.getPromotionalPrice())
                .status(promotionInDto.getStatus())
                .startTime(promotionInDto.getStartTime())
                .endTime(promotionInDto.getEndTime())
                .totalStock(promotionInDto.getTotalStock())
                .availableStock(promotionInDto.getAvailableStock())
                .lockStock(promotionInDto.getLockStock())
                .imageUrl(promotionInDto.getImageUrl())
                .build();
    }
}
