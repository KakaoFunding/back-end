package org.kakaoshare.backend.domain.order.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
    COMPLETE_PAYMENT("결제 완료"),
    PURCHASE_DECISION("구매 결정"),
    CANCELLATION_RETURN_EXCHANGE("취소/환불/교환");

    private final String description;

    OrderStatus(final String description) {
        this.description = description;
    }

    public boolean canceled() {
        return this.equals(CANCELLATION_RETURN_EXCHANGE);
    }
}
