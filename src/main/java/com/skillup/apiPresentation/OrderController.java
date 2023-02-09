package com.skillup.apiPresentation;


import com.skillup.apiPresentation.dto.in.OrderInDto;
import com.skillup.apiPresentation.dto.out.OrderOutDto;
import com.skillup.apiPresentation.util.ResponseUtil;
import com.skillup.apiPresentation.util.SnowFlake;
import com.skillup.application.order.OrderApplication;
import com.skillup.domian.order.OrderDomain;
import com.skillup.domian.order.OrderService;
import com.skillup.domian.order.util.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/order")
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

    // 锁库存下订单是一体的也是秒杀的核心，可以直接跳过购物车下单，其他信息（电话，地址）可以后面付款时再修改
    // 因为创建订单时，不仅要操作订单，还要操作promotion和stock，是一个聚合的业务，所以要引入 application层
    // application 层来实现domain层互相调用的关系，来保持domain层的纯净
    @PostMapping
    public ResponseEntity<OrderOutDto> createOrder(@RequestBody OrderInDto orderInDto) {
        OrderDomain orderDomain = orderApplication.createOrder(toDomain(orderInDto));
        // TODO: should have change response status based on order status
        return ResponseEntity.status(ResponseUtil.SUCCESS).body(toOutDto(orderDomain));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<OrderOutDto> getOrderById(@PathVariable("id") Long orderNumber) {
        log.info("Get Order from DataCenter : {}, Machine: {} ", dataCenter, machineNum);
        OrderDomain orderDomain = orderService.getOrderById(orderNumber);

        if (Objects.isNull(orderDomain)) {
            return ResponseEntity.status(ResponseUtil.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(ResponseUtil.SUCCESS).body(toOutDto(orderDomain));
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

    private OrderOutDto toOutDto(OrderDomain orderDomain) {
        return OrderOutDto.builder()
                .orderNumber(orderDomain.getOrderNumber().toString()) // 转string来解决前端精度丢失问题
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
