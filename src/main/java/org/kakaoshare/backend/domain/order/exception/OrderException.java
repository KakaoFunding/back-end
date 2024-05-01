package org.kakaoshare.backend.domain.order.exception;

public class OrderException extends RuntimeException {
    private final OrderErrorCode errorCode;

    public OrderException(final OrderErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
