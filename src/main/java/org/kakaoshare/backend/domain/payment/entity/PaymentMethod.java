package org.kakaoshare.backend.domain.payment.entity;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum PaymentMethod {
    KAKAO_PAY("카카오페이");

    private final String description;

    PaymentMethod(final String description) {
        this.description = description;
    }

    public static List<String> getNames() {
        return Arrays.stream(values())
                .map(PaymentMethod::name)
                .toList();
    }
}
