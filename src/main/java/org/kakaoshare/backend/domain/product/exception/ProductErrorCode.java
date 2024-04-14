package org.kakaoshare.backend.domain.product.exception;

import lombok.Getter;

@Getter
public enum ProductErrorCode {
    NOT_FOUND("상품을 찾을 수 없습니다.");

    private final String message;

    ProductErrorCode(final String message) {
        this.message = message;
    }
}
