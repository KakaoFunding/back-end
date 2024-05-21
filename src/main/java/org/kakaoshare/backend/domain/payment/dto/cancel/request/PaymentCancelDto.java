package org.kakaoshare.backend.domain.payment.dto.cancel.request;

import org.kakaoshare.backend.domain.payment.entity.Payment;

public record PaymentCancelDto(String tid, Long amount) {
    public PaymentCancelDto {
    }

    public static PaymentCancelDto from(final Payment payment) {
        return new PaymentCancelDto(payment.getPaymentNumber(), payment.getTotalPrice());
    }

    public static PaymentCancelDto of(final Payment payment, final Long amount) {
        return new PaymentCancelDto(payment.getPaymentNumber(), amount);
    }
}
