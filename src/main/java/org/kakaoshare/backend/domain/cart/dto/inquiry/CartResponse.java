package org.kakaoshare.backend.domain.cart.dto.inquiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.cart.entity.Cart;

@Getter
@Builder
@AllArgsConstructor
public class CartResponse {
    private Long cartId;
    private Long productId;
    private Long optionId;
    private Long optionDetailId;
    private String productName;
    private String brandName;
    private int quantity;
    private Long productPrice;
    private String imageUrl;
    private String optionName;
    private String optionDetailName;
    private Long totalPrice;

    public static CartResponse from(Cart cart){
        Long totalPrice = cart.calculateTotalPrice();

        return CartResponse.builder()
                .cartId(cart.getCartId())
                .productId(cart.getProduct().getProductId())
                .optionId(cart.getOption() != null ? cart.getOption().getOptionsId() : null)
                .optionDetailId(cart.getOptionDetail() != null ? cart.getOptionDetail().getOptionDetailId() : null)
                .productName(cart.getProduct().getName())
                .quantity(cart.getItemCount())
                .brandName(cart.getProduct().getBrandName())
                .productPrice(cart.getProduct().getPrice())
                .imageUrl(cart.getProduct().getPhoto())
                .totalPrice(totalPrice)
                .optionName(cart.getOption() != null ? cart.getOption().getName() : null)
                .optionDetailName(cart.getOptionDetail() != null ? cart.getOptionDetail().getName() : null)
                .build();
    }
}
