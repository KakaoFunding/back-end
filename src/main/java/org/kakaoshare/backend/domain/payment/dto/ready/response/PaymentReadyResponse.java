package org.kakaoshare.backend.domain.payment.dto.ready.response;

public record PaymentReadyResponse(String tid, String redirectUrl, String orderNumber) {
}
