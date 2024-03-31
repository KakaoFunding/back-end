package org.kakaoshare.backend.domain.payment.exception;

public enum PaymentErrorCode {
    INVALID_AMOUNT("결제 금액이 올바르지 않습니다.");

    private final String message;

    PaymentErrorCode(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
