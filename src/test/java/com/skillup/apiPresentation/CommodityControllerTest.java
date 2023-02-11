package com.skillup.apiPresentation;

import com.skillup.apiPresentation.dto.out.CommodityOutDto;
import com.skillup.domain.commodity.CommodityDomain;
import com.skillup.domain.commodity.CommodityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommodityControllerTest {

    @InjectMocks
    CommodityController commodityController;

    @Mock
    CommodityService commodityService;

    private static String PROMOTION_ID = "123";


    @Test
    void getCommoditySuccess200() {
        CommodityDomain commodityDomain = CommodityDomain.builder().commodityId(PROMOTION_ID).price(12).build();
        when(commodityService.getCommodityById(PROMOTION_ID)).thenReturn(commodityDomain);

        ResponseEntity<CommodityOutDto> responseEntity = commodityController.getCommodity(PROMOTION_ID);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        CommodityOutDto outDto = responseEntity.getBody();
        assertThat(outDto.getCommodityId()).isEqualTo(PROMOTION_ID);
        assertThat(outDto.getPrice()).isEqualTo(12);
    }

    @Test
    void getCommodityFailed400() {
        when(commodityService.getCommodityById(PROMOTION_ID)).thenReturn(null);

        ResponseEntity<CommodityOutDto> responseEntity = commodityController.getCommodity(PROMOTION_ID);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isEqualTo(null);
    }
}