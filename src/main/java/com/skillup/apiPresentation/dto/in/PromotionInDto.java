package com.skillup.apiPresentation.dto.in;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionInDto {

    String promotionId;

    String promotionName;

    String commodityId;

    Integer originalPrice;

    Integer promotionPrice;

    LocalDateTime startTime;

    LocalDateTime endTime;

    Integer status;

    Long totalStock;

    Long avaliableStock;

    Long lockStock;

    String imageUrl;

}
