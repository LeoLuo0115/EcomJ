package com.skillup.application.order;

import com.skillup.domain.order.OrderDomain;
import com.skillup.domain.order.OrderService;
import com.skillup.domain.order.util.OrderStatus;
import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionService;
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

    @Transactional
    public OrderDomain createBuyNowOrder(OrderDomain orderDomain) {
        // 1. check promotion exist
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
        // 3. fulfillment order detail
        orderDomain.setCreateTime(LocalDateTime.now());
        orderDomain.setOrderStatus(OrderStatus.CREATED);
        OrderDomain savedOrderDomain = orderService.createOrder(orderDomain);
        return savedOrderDomain;
    }
}
