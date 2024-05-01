package org.kakaoshare.backend.domain.payment.dto.kakaopay.cancel.request;

public record KakaoPayCancelRequest(String cid, String tid, Long cancel_amount, Long cancel_tax_free_amount) {
}
