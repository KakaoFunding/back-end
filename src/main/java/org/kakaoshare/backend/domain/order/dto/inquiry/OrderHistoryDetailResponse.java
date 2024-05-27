package org.kakaoshare.backend.domain.order.dto.inquiry;

import org.kakaoshare.backend.domain.payment.dto.inquiry.PaymentHistoryDto;

public record OrderHistoryDetailResponse(OrderHistoryDetailDto order, PaymentHistoryDto payment) {
}
