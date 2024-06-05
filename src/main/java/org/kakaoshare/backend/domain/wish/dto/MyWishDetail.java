package org.kakaoshare.backend.domain.wish.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public final class MyWishDetail {
    private final boolean isPublic;
    private final WishDetail wishDetail;
    @QueryProjection
    public MyWishDetail(final boolean isPublic, final WishDetail wishDetail) {
        this.isPublic = isPublic;
        this.wishDetail = wishDetail;
    }

    public boolean isPublic() {
        return isPublic;
    }
}