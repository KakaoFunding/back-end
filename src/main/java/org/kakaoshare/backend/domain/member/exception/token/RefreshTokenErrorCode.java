package org.kakaoshare.backend.domain.member.exception.token;

import lombok.Getter;
import org.kakaoshare.backend.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum RefreshTokenErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "리프레시 토큰이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    RefreshTokenErrorCode(final HttpStatus httpStatus, final String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
