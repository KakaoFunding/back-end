package org.kakaoshare.backend.domain.product.dto;

public record ProductSummaryResponse(String brandName, String photo, String name, Long price) {
    public ProductSummaryResponse {
    }
}
