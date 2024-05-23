package org.kakaoshare.backend.domain.cart.dto.delete;

import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.cart.dto.CartIdResponse;
import org.kakaoshare.backend.domain.cart.entity.Cart;

@Getter
@Builder
public class CartDeleteResponse extends CartIdResponse {
    public CartDeleteResponse(Long cartId) {
        super(cartId);
    }
}
