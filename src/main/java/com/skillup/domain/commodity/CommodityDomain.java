package com.skillup.domain.commodity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommodityDomain {
    String commodityId;
    String commodityName;
    String description;
    int price;
    String image_url;

}
