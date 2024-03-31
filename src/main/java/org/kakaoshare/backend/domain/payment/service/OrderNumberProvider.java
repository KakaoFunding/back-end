package org.kakaoshare.backend.domain.payment.service;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class OrderNumberProvider {
    private static final int ORDER_DETAIL_KEY_LENGTH = 10;

    public String createOrderDetailKey() {
        return UUID.randomUUID().toString()
                .substring(0, ORDER_DETAIL_KEY_LENGTH);
    }

    public String createOrderNumber() {
        return String.valueOf(LocalDateTime.now().getNano());
    }
}
