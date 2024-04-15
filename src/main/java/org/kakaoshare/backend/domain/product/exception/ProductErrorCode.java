package org.kakaoshare.backend.domain.product.exception;

public enum ProductErrorCode {
    NOT_FOUND_ERROR("존재하지 않는 상품입니다.");
    private final String message;

    ProductErrorCode(String message) {
        this.message = message;
    }
}
