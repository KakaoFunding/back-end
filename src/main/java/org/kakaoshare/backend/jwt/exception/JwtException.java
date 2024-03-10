package org.kakaoshare.backend.jwt.exception;

public class JwtException extends RuntimeException {
    private final JwtErrorCode errorCode;

    public JwtException(final JwtErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}