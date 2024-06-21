package org.kakaoshare.backend.domain.cart.dto.inquiry;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartItemCountResponse {
    private final int itemCount;

    public static CartItemCountResponse from(List<CartResponse> cartResponseList) {
        return CartItemCountResponse.builder()
                .itemCount(cartResponseList.size())
                .build();
    }
}
