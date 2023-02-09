package com.skillup.domian.order;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    public  OrderDomain createOrder(OrderDomain orderDomain) {
        orderRepository.createOrder(orderDomain);
        return orderDomain;
    }

    public OrderDomain getOrderById(Long orderNum) {
        return orderRepository.getOrderById(orderNum);
    }

    public OrderDomain updateOrder(OrderDomain orderDomain) {
        orderRepository.updateOrder(orderDomain);
        return orderDomain;
    }

}
