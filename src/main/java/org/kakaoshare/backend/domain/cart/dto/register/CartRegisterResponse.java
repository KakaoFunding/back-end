package org.kakaoshare.backend.domain.cart.dto.register;

import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.cart.dto.CartIdResponse;
import org.kakaoshare.backend.domain.cart.entity.Cart;

@Getter
public class CartRegisterResponse extends CartIdResponse {
    @Builder
    public CartRegisterResponse(Long cartId) {
        super(cartId);
    }
}
