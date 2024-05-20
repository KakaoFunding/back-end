package org.kakaoshare.backend.domain.cart.dto;

import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.cart.entity.Cart;

@Getter
@Builder
public class CartDeleteResponse {
    private Long cartId;
    private String message;

    public static CartDeleteResponse from(Cart cart){
        return CartDeleteResponse.builder()
                .cartId(cart.getCartId())
                .message("장바구니 상품이 삭제되었습니다.")
                .build();
    }
}
