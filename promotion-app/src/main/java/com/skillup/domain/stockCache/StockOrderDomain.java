package com.skillup.domain.stockCache;

import com.skillup.domain.util.OperationName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockOrderDomain {
    private String promotionId;
    private Long orderNum;
    private OperationName operationName;
}
