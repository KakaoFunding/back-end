package org.kakaoshare.backend.domain.cart.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartClearResponse {
    private boolean success;
    private String message;
}
