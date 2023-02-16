package com.skillup.apiPresentation;

import com.skillup.apiPresentation.dto.in.OrderInDto;
import com.skillup.apiPresentation.dto.in.OrderStatusInDto;
import com.skillup.apiPresentation.dto.out.OrderOutDto;
import com.skillup.apiPresentation.util.SkillResponseUtil;
import com.skillup.apiPresentation.util.SnowFlake;
import com.skillup.application.order.OrderApplication;
import com.skillup.domain.order.OrderDomain;
import com.skillup.domain.order.OrderService;
import com.skillup.domain.order.util.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Value("${order.machine-num}")
    Integer machineNum;

    @Value("${order.data-center}")
    Integer dataCenter;
    @Autowired
    OrderService orderService;

    @Autowired
    OrderApplication orderApplication;

    @Autowired
    SnowFlake snowFlake;

    @GetMapping("/id/{id}")
    public ResponseEntity<OrderOutDto> getOrderById(@PathVariable("id") Long orderNum) {
        log.info("Get order from DataCenter: {}, Machine: {}", dataCenter, machineNum);
        OrderDomain orderDomain = orderService.getOrderById(orderNum);
        if (Objects.isNull(orderDomain)) {
            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(SkillResponseUtil.SUCCESS).body(toOrderOutDto(orderDomain));
    }

    @PostMapping
    public ResponseEntity<OrderOutDto> createBuyNowOrder(@RequestBody OrderInDto orderInDto) {
        OrderDomain orderDomain = orderApplication.createBuyNowOrder(toDomain(orderInDto));
        return ResponseEntity.status(SkillResponseUtil.SUCCESS).body(toOrderOutDto(orderDomain));
    }

    @PatchMapping("/pay")
    public ResponseEntity<OrderOutDto> payBuyNowOrder(@RequestBody OrderStatusInDto orderStatusInDto) {
        OrderDomain orderDomain = orderApplication.payBuyNowOrder(orderStatusInDto.getOrderNumber(), orderStatusInDto.existStatus, orderStatusInDto.getExpectStatus());

        if (Objects.isNull(orderDomain)) {
            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(toOrderOutDto(null));
        } else if (orderDomain.getOrderStatus().equals(OrderStatus.PAYED)) {
            return ResponseEntity.status(SkillResponseUtil.SUCCESS).body(toOrderOutDto(orderDomain));
        } else if (orderDomain.getOrderStatus().equals(OrderStatus.CREATED)){
            return ResponseEntity.status(SkillResponseUtil.INTERNAL_ERROR).body(toOrderOutDto(orderDomain));
        } else if (orderDomain.getOrderStatus().equals(OrderStatus.OVERTIME)) {
            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(toOrderOutDto(orderDomain));
        } else {
            return ResponseEntity.status(SkillResponseUtil.INTERNAL_ERROR).body(toOrderOutDto(orderDomain));
        }
    }

    private OrderDomain toDomain(OrderInDto orderInDto) {
        return OrderDomain.builder()
                .orderNumber(snowFlake.nextId())
                .userId(orderInDto.getUserId())
                .promotionId(orderInDto.getPromotionId())
                .promotionName(orderInDto.getPromotionName())
                .orderAmount(orderInDto.getOrderAmount())
                .orderStatus(OrderStatus.READY)
                .build();
    }

    private OrderOutDto toOrderOutDto(OrderDomain orderDomain) {
        return OrderOutDto.builder()
                .orderNumber(orderDomain.getOrderNumber().toString())
                .userId(orderDomain.getUserId())
                .promotionId(orderDomain.getPromotionId())
                .promotionName(orderDomain.getPromotionName())
                .orderAmount(orderDomain.getOrderAmount())
                .orderStatus(orderDomain.getOrderStatus().code)
                .createTime(orderDomain.getCreateTime())
                .payTime(orderDomain.getPayTime())
                .build();
    }
}
