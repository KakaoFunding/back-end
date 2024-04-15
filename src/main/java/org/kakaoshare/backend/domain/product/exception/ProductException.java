package org.kakaoshare.backend.domain.product.exception;

public class ProductException extends RuntimeException {
    private final ProductErrorCode errorCode;

    public ProductException(ProductErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
