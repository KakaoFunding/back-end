package org.kakaoshare.backend.domain.member.exception.token;

import lombok.Getter;

@Getter
public enum RefreshTokenErrorCode {
    NOT_FOUND("리프레시 토큰이 없습니다.");

    private final String message;

    RefreshTokenErrorCode(final String message) {
        this.message = message;
    }
}
