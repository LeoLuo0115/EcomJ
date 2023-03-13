package com.skillup.apiPresentation;

import com.skillup.apiPresentation.dto.in.PromotionInDto;
import com.skillup.apiPresentation.dto.out.PormotionOutDto;
import com.skillup.apiPresentation.util.ResponseUtil;
import com.skillup.application.promotionCache.CacheApplication;
import com.skillup.domain.promotionSql.PromotionDomain;
import com.skillup.domain.promotionSql.PromotionService;
import com.skillup.domain.stockCache.StockCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    CacheApplication cacheApplication;

    @Autowired
    StockCacheService stockCacheService;

    @PostMapping
    ResponseEntity<PormotionOutDto> createPromotion(@RequestBody PromotionInDto promotionInDto) {
        PromotionDomain promotionDomain = promotionService.registry(toDomain(promotionInDto));
        return ResponseEntity.status(ResponseUtil.SUCCESS).body(toOutDto(promotionDomain));
    }

    @GetMapping("/id/{id}")
    ResponseEntity<PormotionOutDto> getPromotionById(@PathVariable("id") String promotionId) {
        PromotionDomain promotionDomain = cacheApplication.getPromotionById(promotionId);
        if (Objects.isNull(promotionDomain)) {
            return ResponseEntity.status(ResponseUtil.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(ResponseUtil.SUCCESS).body(toOutDto(promotionDomain));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PormotionOutDto>> getPromotionByStatus(@PathVariable("status") Integer promotionStatus) {
        // TODO: ADD CACHE SUPPORT
        List<PromotionDomain> promotionDomainList = promotionService.getPromotionByStatus(promotionStatus);
        return ResponseEntity.status(ResponseUtil.SUCCESS).body(
                promotionDomainList.stream().map(this::toOutDto).collect(Collectors.toList())
                //Collectors.toList()
                //官方解释：返回将输入元素累积到新列表中的收集器。 不保证返回的List 的类型、可变性、可序列化性或线程安全性 ；如果需要对返回的列表进行更多控制，请使用toCollection(Supplier)。
        );
    }

    @PostMapping("/lock/id/{id}")
    public ResponseEntity<Boolean> lockStock(@PathVariable("id") String id) {
        // 1. check promotion existing
        PromotionDomain promotionDomain = cacheApplication.getPromotionById(id);
        if (Objects.isNull(promotionDomain)) {
            return ResponseEntity.status(ResponseUtil.BAD_REQUEST).body(false);
        }
        // 2 boolean isLocked = promotionService.lockStock(id);
        boolean isLocked = stockCacheService.lockStock(id);
        return ResponseEntity.status(ResponseUtil.SUCCESS).body(isLocked);
    }


    // 真正的扣除库存（lock stock - 1），规定时间下单成功, 不需要触及 available stock，我们用lock stock 这个中间量来过度真正扣减库存
    // 扣除库存一定是在数据库层面来做，如果扣减成功 lock stock - 1, 那么永远不可能还给 available stock, available stock等于真正的少了一个
    @PostMapping("/deduct/id/{id}")
    public ResponseEntity<Boolean> deductStock(@PathVariable("id") String id) {
        // 1. check promotion
        PromotionDomain promotionDomain = promotionService.getPromotionById(id);
        if (Objects.isNull(promotionDomain)) {
            return ResponseEntity.status(ResponseUtil.BAD_REQUEST).body(false);
        }
        boolean isDeducted = promotionService.deductStock(id);
        return ResponseEntity.status(ResponseUtil.SUCCESS).body(isDeducted);
    }

    /**
     * Revert Available Stock in Cache
     */
    @PostMapping("/revert/id/{id}")
    public ResponseEntity<Boolean> revertStock(@PathVariable("id") String id) {
        // 1. check promotion
        PromotionDomain promotionDomain = cacheApplication.getPromotionById(id);
        if (Objects.isNull(promotionDomain)) {
            return ResponseEntity.status(ResponseUtil.BAD_REQUEST).body(false);
        }
        boolean isReverted = stockCacheService.revertStock(id);
        return ResponseEntity.status(ResponseUtil.SUCCESS).body(isReverted);
    }


    private PromotionDomain toDomain(PromotionInDto promotionInDto) {
        return PromotionDomain.builder()
                .promotionId(UUID.randomUUID().toString())
                .promotionName(promotionInDto.getPromotionName())
                .commodityId(promotionInDto.getCommodityId())
                .startTime(promotionInDto.getStartTime())
                .endTime(promotionInDto.getEndTime())
                .originalPrice(promotionInDto.getOriginalPrice())
                .promotionalPrice(promotionInDto.getPromotionalPrice())
                .totalStock(promotionInDto.getTotalStock())
                .availableStock(promotionInDto.getAvailableStock())
                .lockStock(promotionInDto.getLockStock())
                .imageUrl(promotionInDto.getImageUrl())
                .status(promotionInDto.getStatus())
                .build();
    }

    private PormotionOutDto toOutDto(PromotionDomain promotionDomain) {
        return PormotionOutDto.builder()
                .promotionId(promotionDomain.getPromotionId())
                .promotionName(promotionDomain.getPromotionName())
                .commodityId(promotionDomain.getCommodityId())
                .originalPrice(promotionDomain.getOriginalPrice())
                .promotionalPrice(promotionDomain.getPromotionalPrice())
                .status(promotionDomain.getStatus())
                .startTime(promotionDomain.getStartTime())
                .endTime(promotionDomain.getEndTime())
                .totalStock(promotionDomain.getTotalStock())
                .availableStock(promotionDomain.getAvailableStock())
                .lockStock(promotionDomain.getLockStock())
                .imageUrl(promotionDomain.getImageUrl())
                .build();
    }


}
