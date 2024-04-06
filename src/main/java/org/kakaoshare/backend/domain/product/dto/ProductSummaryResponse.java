package org.kakaoshare.backend.domain.product.dto;

public record ProductSummaryResponse(String brandName, String name, Long price) {
    public ProductSummaryResponse {
    }
}
