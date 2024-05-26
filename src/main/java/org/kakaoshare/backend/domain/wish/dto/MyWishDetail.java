package org.kakaoshare.backend.domain.wish.dto;

import com.querydsl.core.annotations.QueryProjection;

public final class MyWishDetail extends AbstractWishDetail {
    private final boolean isPublic;

    @QueryProjection
    public MyWishDetail(Long wishId,
                        Long productId,
                        String productName,
                        Long productPrice,
                        String productPhoto,
                        boolean isPublic) {
        super(wishId, productId, productName, productPrice, productPhoto);
        this.isPublic = isPublic;
    }

    public boolean isPublic() {
        return isPublic;
    }
}