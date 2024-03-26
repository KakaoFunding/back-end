package org.kakaoshare.backend.domain.payment.dto.ready.response;

import org.kakaoshare.backend.domain.payment.dto.OrderDetail;

import java.util.List;


public record PaymentReadyResponse(String tid, List<OrderDetail> details, String redirectUrl, String orderNumber) {
}
