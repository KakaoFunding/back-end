package org.kakaoshare.backend.domain.cart.dto.delete;

import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.cart.entity.Cart;

@Getter
@Builder
public class CartDeleteResponse {
    private Long cartId;

    public static CartDeleteResponse from(Cart cart){
        return CartDeleteResponse.builder()
                .cartId(cart.getCartId())
                .build();
    }
}
