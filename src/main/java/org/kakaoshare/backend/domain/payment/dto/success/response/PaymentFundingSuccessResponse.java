package org.kakaoshare.backend.domain.payment.dto.success.response;

import org.kakaoshare.backend.domain.product.dto.ProductSummaryResponse;

public record PaymentFundingSuccessResponse(PaymentSuccessReceiver receiver, ProductSummaryResponse product, Long attributeAmount) {
}
