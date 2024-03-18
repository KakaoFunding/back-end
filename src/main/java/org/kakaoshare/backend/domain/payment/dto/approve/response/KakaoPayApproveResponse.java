package org.kakaoshare.backend.domain.payment.dto.approve.response;

import java.time.LocalDateTime;

public record KakaoPayApproveResponse(
        String aid,
        String tid,
        String cid,
        String sid,
        String partner_order_id,
        String partner_user_id,
        String payment_method_type,
        Amount amount,
        Card card_info,
        String item_name,
        String item_code,
        Integer quantity,
        LocalDateTime create_at,
        LocalDateTime approved_at,
        String payload
) {
}
