package org.kakaoshare.backend.domain.member.dto.oauth.token;

import org.kakaoshare.backend.domain.member.entity.token.RefreshToken;

public record RefreshTokenDto(String value, Long expiration) {
    public static RefreshTokenDto from(final RefreshToken refreshToken) {
        return new RefreshTokenDto(refreshToken.getValue(), refreshToken.getExpiration());
    }
}
