package com.skillup.infrastructure.repoImpl;

import com.skillup.domain.order.OrderDomain;
import com.skillup.domain.order.OrderRepository;
import com.skillup.domain.order.util.OrderStatus;
import com.skillup.infrastructure.jooq.tables.Orders;
import com.skillup.infrastructure.jooq.tables.records.OrdersRecord;
import org.jooq.DSLContext;
import org.jooq.TableRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class jooqOrderRepo implements OrderRepository {

    @Autowired
    DSLContext dslContext;

    private static final Orders O_T = new Orders();
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void createOrder(OrderDomain orderDomain) {
        dslContext.executeInsert(toRecord(orderDomain));
    }


    @Override
    public OrderDomain getOrder(Long id) {
        return dslContext.selectFrom(O_T).where(O_T.ORDER_NUMBER.eq(id)).fetchOptional(this::toDomain).orElse(null);
    }

    private OrderDomain toDomain(OrdersRecord ordersRecord) {
        return OrderDomain.builder()
                .orderNumber(ordersRecord.getOrderNumber())
                .orderStatus(OrderStatus.cachedStatus.get(ordersRecord.getOrderStatus()))
                .promotionId(ordersRecord.getPromotionId())
                .promotionName(ordersRecord.getPromotionName())
                .userId(ordersRecord.getUserId())
                .orderAmount(ordersRecord.getOrderAmount())
                .createTime(ordersRecord.getCreateTime())
                .payTime(ordersRecord.getPayTime())
                .build();
    }

    private OrdersRecord toRecord(OrderDomain orderDomain) {
        return new OrdersRecord(
                orderDomain.getOrderNumber(),
                orderDomain.getOrderStatus().code,
                orderDomain.getPromotionId(),
                orderDomain.getPromotionName(),
                orderDomain.getUserId(),
                orderDomain.getOrderAmount(),
                orderDomain.getCreateTime(),
                orderDomain.getPayTime()
        );
    }
}
