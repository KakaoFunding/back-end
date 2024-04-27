package org.kakaoshare.backend.domain.payment.dto;

import org.kakaoshare.backend.domain.payment.dto.ready.request.PaymentFundingReadyRequest;

public record FundingOrderDetail(Long fundingId) {
    public static FundingOrderDetail from(final PaymentFundingReadyRequest paymentFundingReadyRequest) {
        return new FundingOrderDetail(paymentFundingReadyRequest.fundingId());
    }
}
