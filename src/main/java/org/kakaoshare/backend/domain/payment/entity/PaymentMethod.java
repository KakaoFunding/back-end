package org.kakaoshare.backend.domain.payment.entity;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    KAKAO_PAY("카카오페이");

    private final String description;

    PaymentMethod(final String description) {
        this.description = description;
    }
}
