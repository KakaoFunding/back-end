package org.kakaoshare.backend.domain.cart.dto;

import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.cart.entity.Cart;

@Getter
@Builder
public class CartResponse {
    private Long cartId;
    private Long optionId;
    private Long optionDetailId;
    private String productName;
    private String brandName;
    private int quantity;
    private Long price;
    private String imageUrl;
    private String optionName;
    private String optionDetailName;

    public static CartResponse from(Cart cart){
        Long additionalPrice = cart.getOptionDetail() != null ? cart.getOptionDetail().getAdditionalPrice() : 0;
        Long totalPrice = cart.getItemCount() * (cart.getProduct().getPrice() + additionalPrice);

        return CartResponse.builder()
                .cartId(cart.getCartId())
                .optionId(cart.getOption().getOptionsId())
                .optionDetailId(cart.getOptionDetail().getOptionDetailId())
                .productName(cart.getProduct().getName())
                .quantity(cart.getItemCount())
                .brandName(cart.getProduct().getBrandName())
                .price(totalPrice)
                .imageUrl(cart.getProduct().getPhoto())
                .optionName(cart.getOption() != null ? cart.getOption().getName() : null)
                .optionDetailName(cart.getOptionDetail() != null ? cart.getOptionDetail().getName() : null)
                .build();
    }
}
