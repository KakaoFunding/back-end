package org.kakaoshare.backend.domain.funding.dto;

import org.kakaoshare.backend.domain.product.dto.ProductSummaryResponse;

public record FundingSummaryResponse(ProductSummaryResponse product, Long attributeAmount) {
}
