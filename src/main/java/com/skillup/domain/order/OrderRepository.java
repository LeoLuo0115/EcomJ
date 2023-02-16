package com.skillup.domain.order;

public interface OrderRepository {
    void createOrder(OrderDomain orderDomain);

    OrderDomain getOrder(Long id);
}
