package com.skillup.application.order;

import com.skillup.domain.order.OrderDomain;
import com.skillup.domain.order.OrderDomainService;
import com.skillup.domain.order.util.OrderStatus;
import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class OrderApplication {

    @Autowired
    PromotionDomainService promotionDomainService;

    @Autowired
    OrderDomainService orderDomainService;

    @Transactional
    public OrderDomain createBuyNowOrder(OrderDomain orderDomain){
        PromotionDomain promotionById = promotionDomainService.getPromotionById(orderDomain.getPromotionId());
        if (Objects.isNull(promotionById)) {
            orderDomain.setOrderStatus(OrderStatus.ITEM_ERROR);
            return orderDomain;
        }
        boolean isLocked = promotionDomainService.lockstock(orderDomain.getPromotionId());

        if (!isLocked) {
            orderDomain.setOrderStatus(OrderStatus.OUT_OF_STOCK);
            return orderDomain;
        }

        orderDomain.setCreateTime(LocalDateTime.now());
        orderDomain.setOrderStatus(OrderStatus.CREATED);
        OrderDomain savedOrderDomain = orderDomainService.createOrder(orderDomain);
        return savedOrderDomain;
    }

}
