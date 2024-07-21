package org.kakaoshare.backend.domain.funding.dto.preview.request;

public record FundingProductDto(Long goalAmount, Long remainAmount, Long productId) {
    public FundingProductDto {
    }
}
