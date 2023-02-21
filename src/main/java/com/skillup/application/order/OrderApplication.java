package com.skillup.application.order;

import com.skillup.domian.order.OrderDomain;
import com.skillup.domian.order.OrderService;
import com.skillup.domian.order.util.OrderStatus;
import com.skillup.domian.promotionSql.PromotionDomain;
import com.skillup.domian.promotionSql.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class OrderApplication {
    @Autowired
    PromotionService promotionService;

    @Autowired
    OrderService orderService;


    // 事务来保障两个service的一致性，事务的性质
    @Transactional
    public OrderDomain createOrder(OrderDomain orderDomain) {
        // 1. check promotion exists
        PromotionDomain promotionDomain = promotionService.getPromotionById(orderDomain.getPromotionId());

        if (Objects.isNull(promotionDomain)) {
            orderDomain.setOrderStatus(OrderStatus.ITEM_ERROR);
            return orderDomain;
        }

        // 2. lock stock
        boolean isLocked = promotionService.lockStock(orderDomain.getPromotionId());

        if (!isLocked) {
            orderDomain.setOrderStatus(OrderStatus.OUT_OF_STOCK);
            return orderDomain;
        }

        // 3. lock success, return detailed info
        orderDomain.setCreateTime(LocalDateTime.now());
        orderDomain.setOrderStatus(OrderStatus.CREATED);
        OrderDomain savedOrderDomain = orderService.createOrder(orderDomain);

        return savedOrderDomain;
    }

}
