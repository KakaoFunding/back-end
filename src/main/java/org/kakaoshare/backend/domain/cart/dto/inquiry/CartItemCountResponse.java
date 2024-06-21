package org.kakaoshare.backend.domain.cart.dto.inquiry;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartItemCountResponse {
    private final int count;

    public static CartItemCountResponse from(int count) {
        return CartItemCountResponse.builder()
                .count(count)
                .build();
    }
}
