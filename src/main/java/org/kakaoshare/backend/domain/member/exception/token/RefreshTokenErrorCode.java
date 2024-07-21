package org.kakaoshare.backend.domain.member.exception.token;

import lombok.Getter;
import org.kakaoshare.backend.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum RefreshTokenErrorCode implements ErrorCode {
    NOT_FOUND(CODE_PREFIX + "005", HttpStatus.NOT_FOUND, "리프레시 토큰을 찾을 수 없습니다."),
    INVALID(CODE_PREFIX + "006", HttpStatus.BAD_REQUEST, "유효하지 않은 리프레시 토큰입니다."),
    EXPIRED(CODE_PREFIX + "007", HttpStatus.BAD_REQUEST, "만료된 리프레시 토큰입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    RefreshTokenErrorCode(final String code, final HttpStatus httpStatus, final String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
