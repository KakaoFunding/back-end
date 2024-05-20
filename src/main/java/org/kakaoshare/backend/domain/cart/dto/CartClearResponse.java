package org.kakaoshare.backend.domain.cart.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartClearResponse {
    private boolean success;
    private String message;

    public static CartClearResponse from(){
        return CartClearResponse.builder()
                .success(true)
                .message("장바구니의 모든 상품이 삭제 되었습니다.")
                .build();
    }
}
