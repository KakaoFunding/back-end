package org.kakaoshare.backend.domain.payment.dto.ready.request;

public record PaymentFundingReadyRequest(Long fundingId, Integer amount) {
}
