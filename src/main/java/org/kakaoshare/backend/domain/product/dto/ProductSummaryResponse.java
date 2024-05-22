package org.kakaoshare.backend.domain.product.dto;

import org.kakaoshare.backend.domain.product.entity.Product;

public record ProductSummaryResponse(String brandName, String photo, String name, Long price) {
    public ProductSummaryResponse {
    }

    public static ProductSummaryResponse from(final Product product) {
        return new ProductSummaryResponse(product.getBrandName(), product.getPhoto(), product.getName(), product.getPrice());
    }
}
