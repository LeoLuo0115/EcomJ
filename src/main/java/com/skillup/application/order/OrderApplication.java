package com.skillup.application.order;

import com.alibaba.fastjson.JSON;
import com.skillup.application.order.mq.MqRepo;
import com.skillup.application.promotionCache.util.CacheDomainMapper;
import com.skillup.domain.order.OrderDomain;
import com.skillup.domain.order.OrderService;
import com.skillup.domain.order.util.OrderStatus;
import com.skillup.domain.promotionCache.PromotionCacheService;
import com.skillup.domain.promotionSql.PromotionDomain;
import com.skillup.domain.stockCache.StockCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class OrderApplication {

    @Autowired
    PromotionCacheService promotionCacheService;

    @Autowired
    StockCacheService stockCacheService;

    @Autowired
    OrderService orderService;

    @Autowired
    MqRepo mqRepo;

    @Value("${order.topic.create-order}")
    String createOrderTopic;

    @Value("${promotion.topic.deduct-stock}")
    String deductStockTopic;

    // 调缓存不需要 @Transactional
    public OrderDomain createBuyNowOrder(OrderDomain orderDomain) {
        // 1. check promotion exist
        PromotionDomain promotionDomain = CacheDomainMapper.cacheToDomain(promotionCacheService.getPromotionById(orderDomain.getPromotionId()));
        if (Objects.isNull(promotionDomain)) {
            orderDomain.setOrderStatus(OrderStatus.ITEM_ERROR);
            return orderDomain;
        }
        // 2. lock stock
        // TODO:　Idempotent
        boolean isLocked = stockCacheService.lockStock(orderDomain.getPromotionId());
        if (!isLocked) {
            orderDomain.setOrderStatus(OrderStatus.OUT_OF_STOCK);
            return orderDomain;
        }

        //通过 Mq 来异步的往 DB 里写数据
        mqRepo.sendMessageToTopic(createOrderTopic, JSON.toJSONString(orderDomain));
        return orderDomain;
    }

    @Transactional
    public OrderDomain payBuyNowOrder(Long orderNumber, Integer existStatus, Integer expectStatus) {
        OrderDomain orderDomain = orderService.getOrderById(orderNumber);

        if (Objects.isNull(orderDomain)) return null;

        if (!existStatus.equals(OrderStatus.CREATED.code) || !expectStatus.equals(OrderStatus.PAYED.code)) return orderDomain;

        if (orderDomain.getOrderStatus().equals(OrderStatus.CREATED)) {
            boolean isPayed = thirdPartyPay();
            if (!isPayed) return orderDomain;

            // 1. update order to pay
            orderDomain.setOrderStatus(OrderStatus.PAYED);
            orderDomain.setPayTime(LocalDateTime.now());
            orderService.updateOrder(orderDomain);

            // 2. deduct promotion stock
            mqRepo.sendMessageToTopic(deductStockTopic, JSON.toJSONString(orderDomain));

            // 3. return
            return orderDomain;
        }
        return orderDomain;
    }

    public boolean thirdPartyPay() {
        return true;
    }
}
