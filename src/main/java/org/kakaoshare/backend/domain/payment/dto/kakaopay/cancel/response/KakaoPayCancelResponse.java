package org.kakaoshare.backend.domain.payment.dto.kakaopay.cancel.response;

import org.kakaoshare.backend.domain.payment.dto.kakaopay.Amount;

import java.time.LocalDateTime;

public record KakaoPayCancelResponse(String aid, String tid, String cid, String status,
                                     String partner_order_id, String partner_user_id, String payment_method_type,
                                     Amount amount, Amount approved_cancel_amount, Amount canceled_amount, Amount cancel_available_amount,
                                     String item_name, String item_code, Integer quantity, LocalDateTime created_at, LocalDateTime approved_at,
                                     LocalDateTime canceled_at, String payload) {
}
