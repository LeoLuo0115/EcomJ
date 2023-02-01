package com.skillup.domain.promotion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionDomain {

    String promotionId;

    String promotionName;

    String commodityId;

    Integer originalPrice;

    Integer promotionPrice;

    LocalDateTime startTime;

    LocalDateTime endTime;
/*
        0 not start; 1 releasing; 2 end
 */
    Integer status;

    Long totalStock;

    Long avaliableStock;

    Long lockStock;

    String imageUrl;
}