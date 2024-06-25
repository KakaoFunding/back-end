package org.kakaoshare.backend.domain.funding.dto.inquiry.response;

import org.kakaoshare.backend.domain.funding.dto.inquiry.ContributedFundingHistoryDto;
import org.kakaoshare.backend.domain.product.dto.ProductDto;

import java.time.LocalDateTime;

public record ContributedFundingHistoryResponse(ProductDto product,
                                                Long fundingId,
                                                Long fundingDetailId,
                                                Long contributedAmount,
                                                LocalDateTime contributedAt,
                                                String creatorName,
                                                Boolean self,
                                                String status) {
    public static ContributedFundingHistoryResponse of(final ContributedFundingHistoryDto contributedFundingHistoryDto,
                                                       final String providerId) {
        return new ContributedFundingHistoryResponse(
                contributedFundingHistoryDto.productDto(),
                contributedFundingHistoryDto.fundingId(),
                contributedFundingHistoryDto.fundingDetailId(),
                contributedFundingHistoryDto.contributedAmount(),
                contributedFundingHistoryDto.contributedAt(),
                contributedFundingHistoryDto.creatorName(),
                contributedFundingHistoryDto.providerId().equals(providerId),
                contributedFundingHistoryDto.status()
        );
    }
}
