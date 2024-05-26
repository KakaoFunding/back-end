package org.kakaoshare.backend.domain.wish.dto;

import lombok.Getter;

@Getter
public abstract class AbstractWishDetail {
    private final Long wishId;
    private final Long productId;
    private final String productName;
    private final Long productPrice;
    private final String productPhoto;
    
    protected AbstractWishDetail(Long wishId,
                                 Long productId,
                                 String productName,
                                 Long productPrice,
                                 String productPhoto) {
        this.wishId = wishId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productPhoto = productPhoto;
    }
}
