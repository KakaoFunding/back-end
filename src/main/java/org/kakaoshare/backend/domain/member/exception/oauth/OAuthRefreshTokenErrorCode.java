package org.kakaoshare.backend.domain.member.exception.oauth;

import lombok.Getter;
import org.kakaoshare.backend.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum OAuthRefreshTokenErrorCode implements ErrorCode {
    NOT_FOUND(CODE_PREFIX + "010", HttpStatus.NOT_FOUND, "소셜 리프레시 토큰을 찾을 수 없습니다."),
    INVALID(CODE_PREFIX + "011", HttpStatus.NOT_FOUND, "유효하지 않은 소셜 리프레시 토큰입니다."),
    EXPIRED(CODE_PREFIX + "012", HttpStatus.NOT_FOUND, "만료된 소셜 리프레시 토큰입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    OAuthRefreshTokenErrorCode(final String code, final HttpStatus httpStatus, final String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
