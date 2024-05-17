package org.kakaoshare.backend.domain.cart.entity;

public enum CartStatus {
    BEFORE_PAYMENT("결제 전"),
    AFTER_PAYMENT("결제 후");

    private final String description;

    CartStatus(String description) {
        this.description = description;
    }
}
