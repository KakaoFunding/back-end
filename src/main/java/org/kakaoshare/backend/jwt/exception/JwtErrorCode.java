package org.kakaoshare.backend.jwt.exception;

import lombok.Getter;
import org.kakaoshare.backend.common.error.ErrorCode;
import org.springframework.http.HttpStatus;
@Getter
public enum JwtErrorCode implements ErrorCode {
    INVALID(HttpStatus.UNAUTHORIZED,"유효하지 않은 토큰입니다."),
    EXPIRED(HttpStatus.UNAUTHORIZED,"만료된 토큰입니다."),
    UNSUPPORTED(HttpStatus.UNAUTHORIZED,"JWT를 지원하지 않습니다."),
    NOT_FOUND(HttpStatus.UNAUTHORIZED,"토큰을 찾을 수 없습니다.");
    
    private final HttpStatus httpStatus;
    private final String message;
    
    JwtErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
