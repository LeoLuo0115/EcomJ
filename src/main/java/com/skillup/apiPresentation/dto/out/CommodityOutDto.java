package com.skillup.apiPresentation.dto.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommodityOutDto {
    private String commodityId;
    private String commodityName;
    private String description;
    private int price;
    private String imageUrl;

}
