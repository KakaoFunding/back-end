package org.kakaoshare.backend.domain.member.exception.token;

public class RefreshTokenException extends RuntimeException {
    private final RefreshTokenErrorCode errorCode;

    public RefreshTokenException(final RefreshTokenErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
