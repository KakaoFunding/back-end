package org.kakaoshare.backend.domain.funding.dto.preview.response;

import org.kakaoshare.backend.domain.funding.exception.FundingErrorCode;
import org.kakaoshare.backend.domain.funding.exception.FundingException;

public record FundingPreviewAmount(Long totalAmount, Long remainAmount, Long attributeAmount) {
    public FundingPreviewAmount {
        if (remainAmount < 0) {
            throw new FundingException(FundingErrorCode.INVALID_ACCUMULATE_AMOUNT);
        }
    }
    public static FundingPreviewAmount of(final Long totalAmount, final Long attributeAmount) {
        return new FundingPreviewAmount(totalAmount, totalAmount - attributeAmount, attributeAmount);
    }
}
