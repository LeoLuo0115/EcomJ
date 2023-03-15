package com.skillup.application.promotion;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("promotion-app")
public interface PromotionCacheApplication {

    @PostMapping("promotion/lock/id/{id}/order/id/{orderId}")
    public ResponseEntity<Boolean> lockStock(@PathVariable("id") String id, @PathVariable("orderId") Long orderId);

    @PostMapping("promotion/revert/id/{id}/order/id/{orderId}")
    public ResponseEntity<Boolean> revertStock(@PathVariable("id") String id, @PathVariable("orderId") Long orderId);
}
