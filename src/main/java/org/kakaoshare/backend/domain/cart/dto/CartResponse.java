package org.kakaoshare.backend.domain.cart.dto;

import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.cart.entity.Cart;

@Getter
@Builder
public class CartResponse {
    private Long cartId;
    private String productName;
    private String brandName;
    private int quantity;
    private Long price;
    private String imageUrl;

    public static CartResponse from(Cart cart){
        return CartResponse.builder()
                .cartId(cart.getCartId())
                .productName(cart.getProduct().getName())
                .quantity(cart.getItemCount())
                .brandName(cart.getProduct().getBrandName())
                .price(cart.getItemCount() * cart.getProduct().getPrice())
                .imageUrl(cart.getProduct().getPhoto())
                .build();
    }
}
