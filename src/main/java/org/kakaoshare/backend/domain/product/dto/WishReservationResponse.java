package org.kakaoshare.backend.domain.product.dto;

public record WishReservationResponse(Long productId, Integer wishCount) {
    public static WishReservationResponse of(final Long productId, final Integer wishCount) {
        return new WishReservationResponse(productId, wishCount);
    }
}
