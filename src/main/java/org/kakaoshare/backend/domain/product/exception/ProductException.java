package org.kakaoshare.backend.domain.product.exception;

public class ProductException extends RuntimeException {
    private final ProductErrorCode errorCode;

    public ProductException(final ProductErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
