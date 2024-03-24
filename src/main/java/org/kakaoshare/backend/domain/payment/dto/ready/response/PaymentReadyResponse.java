package org.kakaoshare.backend.domain.payment.dto.ready.response;

import org.kakaoshare.backend.domain.payment.dto.PaymentDetail;

import java.util.List;


public record PaymentReadyResponse(String tid, List<PaymentDetail> details, String redirectUrl, String orderNumber) {
}
