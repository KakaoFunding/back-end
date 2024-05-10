package org.kakaoshare.backend.domain.payment.dto.success.response;

import org.kakaoshare.backend.domain.funding.dto.FundingSummaryResponse;

public record PaymentFundingSuccessResponse(Receiver receiver, FundingSummaryResponse summary) {
}
