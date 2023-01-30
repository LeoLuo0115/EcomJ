package com.skillup.apiPresentation;

import com.skillup.apiPresentation.dto.in.OrderInDto;
import com.skillup.apiPresentation.dto.out.OrderOutDto;
import com.skillup.apiPresentation.util.SkillResponseUtil;
import com.skillup.apiPresentation.util.SnowFlake;
import com.skillup.application.order.OrderApplication;
import com.skillup.domain.order.OrderDomain;
import com.skillup.domain.order.OrderDomainService;
import com.skillup.domain.order.util.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController("/order")
@Slf4j
public class OrderController {

    @Value("${order.machine-num}")
    Integer machineNum;

    @Value("${order.data-center}")
    Integer dataCenter;

    @Autowired
    OrderDomainService orderDomainService;

    @Autowired
    OrderApplication orderApplication;
    @Autowired
    SnowFlake snowFlake;

    @GetMapping("/id/{id}")
    public ResponseEntity<OrderOutDto> getOrderById(@PathVariable("id") Long orderNum){
        log.info("get order from DataCenter: {}, machine: {}", dataCenter, machineNum);
        OrderDomain orderDomain  = orderDomainService.getOrderById(orderNum);

        if (Objects.isNull(orderDomain)) {
            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(SkillResponseUtil.SUCCESS).body(toOrderOutDto(orderDomain));
    }

    @PostMapping
    public ResponseEntity<OrderOutDto> createBuyNowOrder(@RequestBody OrderInDto orderInDto){
        OrderDomain buyNowOrder = orderApplication.createBuyNowOrder(toDomain(orderInDto));
        return ResponseEntity.status(200).body(toOrderOutDto(buyNowOrder));

    }

    public OrderDomain toDomain(OrderInDto orderInDto){
        return OrderDomain.builder()
                .orderNumber(snowFlake.nextId())
                .userId(orderInDto.getUserId())
                .promotionId(orderInDto.getPromotionId())
                .promotionName(orderInDto.getPromotionName())
                .orderAmount(orderInDto.getOrderAmount())
                .orderStatus(OrderStatus.READY)
                .build();
    }

    public OrderOutDto toOrderOutDto(OrderDomain od){
        return OrderOutDto.builder()
                .orderAmount(od.getOrderAmount())
                .orderNumber(od.getOrderNumber())
                .userId(od.getUserId())
                .promotionId(od.getPromotionId())
                .promotionName(od.getPromotionName())
                .orderStatus(od.getOrderStatus().code)
                .payTime(od.getPayTime())
                .createTime(od.getCreateTime())
                .build();

    }
}
