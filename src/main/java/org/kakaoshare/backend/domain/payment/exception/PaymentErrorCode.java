package org.kakaoshare.backend.domain.payment.exception;

public enum PaymentErrorCode {
    INVALID_AMOUNT("결제 금액이 올바르지 않습니다."),
    INVALID_OPTION("선택한 옵션이 올바르지 않습니다."),
    NOT_FOUND("결제 내역을 찾을 수 없습니다."),
    ALREADY_REFUND("이미 환불된 내역입니다.");

    private final String message;

    PaymentErrorCode(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
