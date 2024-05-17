package org.kakaoshare.backend.domain.payment.dto.success.response;

import org.kakaoshare.backend.domain.product.dto.ProductSummaryResponse;

public record PaymentFundingSuccessResponse(Receiver receiver, ProductSummaryResponse product, Long attributeAmount) {
}
