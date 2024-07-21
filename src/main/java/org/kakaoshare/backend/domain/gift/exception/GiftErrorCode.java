package org.kakaoshare.backend.domain.gift.exception;

import lombok.Getter;
import org.kakaoshare.backend.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum GiftErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 선물 내역입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    GiftErrorCode(final HttpStatus httpStatus, final String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return "";
    }
}
