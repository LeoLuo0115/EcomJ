package com.skillup.domian.commodity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommodityDomain {
    String commodityId;

    String commodityName;

    String description;

    Integer price;

    String imageUrl;
}
