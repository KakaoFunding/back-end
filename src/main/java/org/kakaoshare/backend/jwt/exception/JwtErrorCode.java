package org.kakaoshare.backend.jwt.exception;

import lombok.Getter;
import org.kakaoshare.backend.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum JwtErrorCode implements ErrorCode {
    NOT_FOUND(CODE_PREFIX + "001", HttpStatus.UNAUTHORIZED,"토큰을 찾을 수 없습니다."),
    INVALID(CODE_PREFIX + "002", HttpStatus.UNAUTHORIZED,"유효하지 않은 토큰입니다."),
    EXPIRED(CODE_PREFIX + "003", HttpStatus.UNAUTHORIZED,"만료된 토큰입니다."),
    UNSUPPORTED(CODE_PREFIX + "004", HttpStatus.UNAUTHORIZED,"JWT를 지원하지 않습니다.");
    
    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
    
    JwtErrorCode(final String code, final HttpStatus httpStatus, final String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}