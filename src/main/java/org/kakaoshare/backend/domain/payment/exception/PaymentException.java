package org.kakaoshare.backend.domain.payment.exception;

public class PaymentException extends RuntimeException {
    private final PaymentErrorCode errorCode;

    public PaymentException(final PaymentErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
