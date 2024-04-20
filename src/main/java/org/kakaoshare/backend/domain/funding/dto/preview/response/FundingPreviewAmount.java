package org.kakaoshare.backend.domain.funding.dto.preview.response;

public record FundingPreviewAmount(Long totalAmount, Long remainAmount, Long attributeAmount) {
    public static FundingPreviewAmount of(final Long totalAmount, final Long attributeAmount) {
        return new FundingPreviewAmount(totalAmount, totalAmount - attributeAmount, attributeAmount);
    }
}
