package org.kakaoshare.backend.domain.product.exception;

import org.kakaoshare.backend.domain.payment.exception.PaymentErrorCode;

public class ProductException extends RuntimeException {
    private final PaymentErrorCode errorCode;

    public ProductException(PaymentErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
