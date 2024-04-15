package org.kakaoshare.backend.domain.product.dto;

import org.kakaoshare.backend.domain.product.entity.Product;

public record WishResponse(Long productId, Integer wishCount) {
    public static WishResponse from(final Product product) {
        return new WishResponse(product.getProductId(), product.getWishCount());
    }
}
