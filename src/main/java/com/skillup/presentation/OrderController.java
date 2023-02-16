package com.skillup.presentation;

import com.skillup.application.order.OrderApplication;
import com.skillup.domain.order.OrderDomain;
import com.skillup.domain.order.OrderService;
import com.skillup.domain.order.util.OrderStatus;
import com.skillup.presentation.dto.in.OrderInDto;
import com.skillup.presentation.dto.out.OrderOutDto;
import com.skillup.presentation.util.ResponseCode;
import com.skillup.presentation.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
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

    @PostMapping
    public ResponseEntity<OrderOutDto> createOrder(@RequestBody OrderInDto orderInDto) {
        OrderDomain orderDomain = orderApplication.createBuyNowOrder(toDomain(orderInDto));
        return ResponseEntity.status(ResponseCode.SUCCESS).body(toOutDto(orderDomain));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<OrderOutDto> getOrderbyId(@PathVariable("id") Long id) {
        log.info("Get order from DataCenter: {}, Machine: {}", dataCenter, machineNum);
        OrderDomain orderDomain = orderService.getOrderById(id);
        if (Objects.isNull(orderDomain)) {
            return ResponseEntity.status(ResponseCode.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(ResponseCode.SUCCESS).body(toOutDto(orderDomain));
    }

    private OrderOutDto toOutDto(OrderDomain orderDomain) {
        return OrderOutDto.builder()
                .orderNumber(orderDomain.getOrderNumber().toString())
                .orderStatus(orderDomain.getOrderStatus().code)
                .promotionId(orderDomain.getPromotionId())
                .promotionName(orderDomain.getPromotionName())
                .userId(orderDomain.getUserId())
                .orderAmount(orderDomain.getOrderAmount())
                .createTime(orderDomain.getCreateTime())
                .payTime(orderDomain.getPayTime())
                .build();
    }

    private OrderDomain toDomain(OrderInDto orderInDto) {
        return OrderDomain.builder()
                .orderNumber(snowFlake.nextId())
                .promotionId(orderInDto.getPromotionId())
                .promotionName(orderInDto.getPromotionName())
                .userId(orderInDto.getUserId())
                .orderAmount(orderInDto.getOrderAmount())
                .orderStatus(OrderStatus.READY)
                .build();
    }
}
