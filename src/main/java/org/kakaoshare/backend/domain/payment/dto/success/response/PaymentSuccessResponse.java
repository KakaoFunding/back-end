package org.kakaoshare.backend.domain.payment.dto.success.response;

import org.kakaoshare.backend.domain.order.dto.OrderSummary;

import java.util.List;

public record PaymentSuccessResponse(Receiver receiver, List<OrderSummary> orders) {
}
