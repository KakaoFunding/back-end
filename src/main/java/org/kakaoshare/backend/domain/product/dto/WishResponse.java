package org.kakaoshare.backend.domain.product.dto;

public record WishResponse(Long productId, Integer wishCount) {
    public static WishResponse of(final Long productId, final Integer wishCount) {
        return new WishResponse(productId, wishCount);
    }
}
