package com.skillup.domain.order;

public interface OrderDomainRepository {

    void createOrder(OrderDomain orderDomain);

    OrderDomain getOrderById(Long id);

    void updateOrder(OrderDomain orderDomain);
}
