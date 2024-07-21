package org.kakaoshare.backend.domain.payment.dto.success.request;

public record PaymentSuccessRequest(String orderNumber, String pgToken, String tid) {
}
