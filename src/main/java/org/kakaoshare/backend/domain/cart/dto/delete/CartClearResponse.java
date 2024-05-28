package org.kakaoshare.backend.domain.cart.dto.delete;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartClearResponse {
    private boolean success;

    public static CartClearResponse from(){
        return CartClearResponse.builder()
                .success(true)
                .build();
    }
}
