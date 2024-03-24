package org.kakaoshare.backend.domain.payment.service;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderNumberProvider {
    private static final int ORDER_NUMBER_LENGTH = 10;

    public String createOrderNumber() {
        // TODO: 3/15/24 주문 번호를 UUID의 해시코드 값으로 설정 (10글자)
        return String.valueOf(UUID.randomUUID().hashCode())
                .substring(0, ORDER_NUMBER_LENGTH);
    }
}
