package org.kakaoshare.backend.domain.wish.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public final class FriendWishDetail{
    private final boolean isWished;
    private final WishDetail wishDetail;
    
    @QueryProjection
    public FriendWishDetail(WishDetail wishDetail, boolean isWished) {
        this.wishDetail=wishDetail;
        this.isWished = isWished;
    }
    
    public boolean isWished() {
        return isWished;
    }
}