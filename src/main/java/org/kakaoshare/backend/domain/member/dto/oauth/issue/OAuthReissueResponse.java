package org.kakaoshare.backend.domain.member.dto.oauth.issue;

import org.kakaoshare.backend.domain.member.dto.oauth.token.RefreshTokenDto;
import org.kakaoshare.backend.domain.member.entity.token.RefreshToken;

public record OAuthReissueResponse(String accessToken, RefreshTokenDto refreshToken) {
    public static OAuthReissueResponse of(final String accessToken, final RefreshToken refreshToken) {
        return new OAuthReissueResponse(accessToken, RefreshTokenDto.from(refreshToken));
    }
}