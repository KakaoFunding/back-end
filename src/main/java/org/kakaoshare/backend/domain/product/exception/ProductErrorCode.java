package org.kakaoshare.backend.domain.product.exception;

import lombok.Getter;
import org.kakaoshare.backend.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum ProductErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다."),
    NOT_FOUND_THUMBNAIL_ERROR(HttpStatus.NOT_FOUND, "존재하지 않는 썸네일입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ProductErrorCode(final HttpStatus httpStatus, final String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return "";
    }
}
