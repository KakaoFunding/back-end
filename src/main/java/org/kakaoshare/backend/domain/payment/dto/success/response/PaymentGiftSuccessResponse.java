package org.kakaoshare.backend.domain.payment.dto.success.response;

import org.kakaoshare.backend.domain.order.dto.OrderSummaryResponse;

import java.util.List;

public record PaymentGiftSuccessResponse(PaymentSuccessReceiver receiver, List<OrderSummaryResponse> orders) {
}
