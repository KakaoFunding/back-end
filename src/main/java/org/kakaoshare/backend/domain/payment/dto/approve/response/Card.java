package org.kakaoshare.backend.domain.payment.dto.approve.response;

public record Card(
        String kakaopay_purchase_corp,
        String kakaopay_purchase_corp_code,
        String kakaopay_issuer_corp,
        String kakaopay_issuer_corp_code,
        String bin,
        String card_type,
        String install_month,
        String approved_id,
        String card_mid,
        String interest_free_install,
        String installment_type,
        String cart_item_code
) {
}
