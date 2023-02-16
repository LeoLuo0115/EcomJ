package com.skillup.application.promotion.mq.subscriber;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDomain {
    private Long orderNumber;

    private String promotionId;

    private String promotionName;

    private String userId;

    private Integer orderAmount;

    private LocalDateTime createTime;

    private LocalDateTime payTime;
}
