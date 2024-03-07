package org.kakaoshare.backend.jwt.exception;

public enum JwtErrorCode {
    INVALID("유효하지 않은 토큰입니다."),
    EXPIRED("만료된 토큰입니다."),
    UNSUPPORTED("JWT를 지원하지 않습니다."),
    NOT_FOUND("토큰을 찾을 수 없습니다.");

    private final String message;

    JwtErrorCode(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
