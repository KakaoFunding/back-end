package org.kakaoshare.backend.domain.cart.dto;

import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.cart.entity.Cart;

@Getter
@Builder
public class CartRegisterResponse {
    private Long cartId;
    private String message;

    public static CartRegisterResponse from(Cart cart){
        return CartRegisterResponse.builder()
                .cartId(cart.getCartId())
                .message("상품이 장바구니에 추가되었습니다.")
                .build();
    }
}
