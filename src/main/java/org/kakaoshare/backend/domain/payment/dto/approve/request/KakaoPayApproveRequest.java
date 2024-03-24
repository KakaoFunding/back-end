package org.kakaoshare.backend.domain.payment.dto.approve.request;

public record KakaoPayApproveRequest(String cid, String cid_secret, String tid,
                                     String partner_order_id, String partner_user_id, String pg_token) {
}
