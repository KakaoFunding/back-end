package org.kakaoshare.backend.domain.cart.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartRegisterRequest {
    private Long productId;
    private Long optionId;
    private Long optionDetailId;
}
