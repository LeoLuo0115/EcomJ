package com.skillup.domain.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderDomainService {

    @Autowired
    OrderDomainRepository orderDomainRepository;


    public OrderDomain getOrderById(Long orderNum) {
        return orderDomainRepository.getOrderById(orderNum);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public OrderDomain createOrder(OrderDomain orderDomain){
        orderDomainRepository.createOrder(orderDomain);
        return orderDomain;
    }


}
