package org.kakaoshare.backend.domain.payment.dto.success.request;

import org.kakaoshare.backend.domain.payment.dto.OrderDetail;

import java.util.List;

public record PaymentSuccessRequest(List<OrderDetail> details, String orderNumber, String pgToken, String tid) {
}
