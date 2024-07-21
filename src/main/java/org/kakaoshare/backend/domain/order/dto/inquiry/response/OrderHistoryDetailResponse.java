package org.kakaoshare.backend.domain.order.dto.inquiry.response;

import org.kakaoshare.backend.domain.order.dto.inquiry.OrderHistoryDetailDto;
import org.kakaoshare.backend.domain.payment.dto.inquiry.PaymentHistoryDto;

public record OrderHistoryDetailResponse(OrderHistoryDetailDto order, PaymentHistoryDto payment) {
}
