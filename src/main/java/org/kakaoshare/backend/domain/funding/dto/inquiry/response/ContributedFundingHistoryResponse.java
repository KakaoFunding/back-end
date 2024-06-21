package org.kakaoshare.backend.domain.funding.dto.inquiry.response;

import com.querydsl.core.annotations.QueryProjection;
import org.kakaoshare.backend.domain.funding.dto.inquiry.ContributedFundingHistoryDto;
import org.kakaoshare.backend.domain.product.dto.ProductDto;

public record ContributedFundingHistoryResponse(ProductDto product, ContributedFundingHistoryDto fundingDetail) {
    @QueryProjection
    public ContributedFundingHistoryResponse {
    }
}
