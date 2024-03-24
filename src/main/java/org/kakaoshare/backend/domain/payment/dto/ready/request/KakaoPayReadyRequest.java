package org.kakaoshare.backend.domain.payment.dto.ready.request;

public record KakaoPayReadyRequest(String cid, String cid_secret, String partner_order_id, String partner_user_id,
                                   String item_name, Integer quantity, Integer total_amount, Integer tax_free_amount,
                                   String approval_url, String cancel_url, String fail_url) {
}
