package org.kakaoshare.backend.domain.funding.dto.inquiry;

import com.querydsl.core.annotations.QueryProjection;
import org.kakaoshare.backend.domain.product.dto.ProductDto;

import java.time.LocalDateTime;

public record ContributedFundingHistoryDto(ProductDto productDto,
                                           Long fundingId,
                                           Long fundingDetailId,
                                           Long contributedAmount,
                                           LocalDateTime contributedAt,
                                           String providerId,
                                           String creatorName,
                                           String status) {
    @QueryProjection
    public ContributedFundingHistoryDto {
    }
}
