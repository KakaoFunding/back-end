package org.kakaoshare.backend.domain.receipt.entity;

import lombok.Getter;
import org.kakaoshare.backend.domain.gift.entity.Gift;
import org.kakaoshare.backend.domain.order.entity.Order;
import org.kakaoshare.backend.domain.payment.entity.Payment;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Receipts {
    private final List<Receipt> values;

    public Receipts(final List<Receipt> values) {
        this.values = values;
    }

    public List<Gift> toGifts(final LocalDateTime expiredAt) {
        return values.stream()
                .map(receipt -> new Gift(expiredAt, receipt))
                .toList();
    }

    public List<Order> toOrders(final Payment payment) {
        return values.stream()
                .map(receipt -> new Order(payment, receipt))
                .toList();
    }
}
