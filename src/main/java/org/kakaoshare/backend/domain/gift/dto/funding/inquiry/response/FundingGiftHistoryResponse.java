package org.kakaoshare.backend.domain.gift.dto.funding.inquiry.response;

import com.querydsl.core.annotations.QueryProjection;
import org.kakaoshare.backend.domain.product.dto.ProductDto;

import java.time.LocalDateTime;

public record FundingGiftHistoryResponse(ProductDto product, Integer quantity, LocalDateTime receivedDate, String status) {
    @QueryProjection
    public FundingGiftHistoryResponse {
    }
}
