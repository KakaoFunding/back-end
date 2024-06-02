package org.kakaoshare.backend.domain.cart.dto;

import lombok.Getter;
import org.kakaoshare.backend.domain.cart.entity.Cart;

@Getter
public abstract class CartIdResponse {
    private Long cartId;

    protected CartIdResponse(Long cartId) {
        this.cartId = cartId;
    }

    public static <T extends CartIdResponse> T from(Cart cart, Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor(Long.class).newInstance(cart.getCartId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid class for cart response", e);
        }
    }
}
