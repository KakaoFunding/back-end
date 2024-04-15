package org.kakaoshare.backend.domain.member.dto.oauth.issue;

import org.kakaoshare.backend.domain.member.entity.token.RefreshToken;

public record IssuedTokenResult(String accessToken, String refreshToken) {
    public static IssuedTokenResult of(final String accessToken, final RefreshToken refreshToken) {
        return new IssuedTokenResult(accessToken, refreshToken.getValue());
    }
}