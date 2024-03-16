package org.kakaoshare.backend.domain.payment.service;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderNumberProvider {
    public String createOrderNumber() {
        return String.valueOf(UUID.randomUUID().hashCode());// TODO: 3/15/24 주문 번호를 UUID의 해시코드 값으로 설정
    }
}
