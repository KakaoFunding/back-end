package org.kakaoshare.backend.domain.cart.dto;

import lombok.Getter;
import org.kakaoshare.backend.domain.cart.dto.delete.CartDeleteResponse;
import org.kakaoshare.backend.domain.cart.dto.register.CartRegisterResponse;
import org.kakaoshare.backend.domain.cart.entity.Cart;

@Getter
public abstract class CartIdResponse {
    private Long cartId;

    protected CartIdResponse(Long cartId) {
        this.cartId = cartId;
    }

    public static CartIdResponse from(Cart cart, Class<? extends CartIdResponse> clazz) {
        if (clazz.equals(CartDeleteResponse.class)) {
            return new CartDeleteResponse(cart.getCartId());
        } else if (clazz.equals(CartRegisterResponse.class)) {
            return new CartRegisterResponse(cart.getCartId());
        }
        throw new IllegalArgumentException("Invalid class for cart response");
    }
}
