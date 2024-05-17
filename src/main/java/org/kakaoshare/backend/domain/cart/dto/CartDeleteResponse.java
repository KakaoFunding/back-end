package org.kakaoshare.backend.domain.cart.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartDeleteResponse {
    private Long cartId;
    private String message;
}
