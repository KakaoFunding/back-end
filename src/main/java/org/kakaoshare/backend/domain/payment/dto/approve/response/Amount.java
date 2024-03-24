package org.kakaoshare.backend.domain.payment.dto.approve.response;

public record Amount(
        Integer total,
        Integer tax_free,
        Integer vat,
        Integer point,
        Integer discount,
        Integer green_deposit
) {
}
