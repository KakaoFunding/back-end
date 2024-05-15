package org.kakaoshare.backend.domain.wish.dto;

import com.querydsl.core.annotations.QueryProjection;

public final class FriendWishDetail extends AbstractWishDetail {
    private final boolean isWished;
    
    @QueryProjection
    public FriendWishDetail(Long wishId, Long productId, String productName, Long productPrice, String productPhoto, boolean isWished) {
        super(wishId, productId, productName, productPrice, productPhoto);
        this.isWished = isWished;
    }
    
    public boolean isWished() {
        return isWished;
    }
}