package org.kakaoshare.backend.domain.payment.service;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class OrderNumberProvider {
    public String createOrderDetailKey() {
        return UUID.randomUUID().toString();
    }

    public String createOrderNumber() {
        return String.valueOf(LocalDateTime.now().getNano());
    }
}
