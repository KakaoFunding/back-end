package org.kakaoshare.backend.domain.wish.dto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class WishDetail {
    private final Long wishId;
    private final Long productId;
    private final String productName;
    private final Long productPrice;
    private final String productPhoto;
    private final String brandName;
    private final Integer wishCount;
    @QueryProjection
    public WishDetail(final Long wishId,
                      final Long productId,
                      final String productName,
                      final Long productPrice,
                      final String productPhoto,
                      final String brandName,
                      final Integer wishCount) {
        this.wishId = wishId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productPhoto = productPhoto;
        this.brandName = brandName;
        this.wishCount = wishCount;
    }
}