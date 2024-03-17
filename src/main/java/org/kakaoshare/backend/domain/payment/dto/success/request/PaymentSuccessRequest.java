package org.kakaoshare.backend.domain.payment.dto.success.request;

import org.kakaoshare.backend.domain.payment.dto.PaymentDetail;

import java.util.List;

public record PaymentSuccessRequest(List<PaymentDetail> details, String orderNumber, String pgToken, String tid) {
}
