package org.kakaoshare.backend.domain.payment.dto.cancel.request;

public record PaymentFundingDetailCancelRequest(Long fundingDetailId, Long amount) {
}
