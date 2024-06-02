package org.kakaoshare.backend.domain.member.dto.oauth.issue;

import org.kakaoshare.backend.domain.member.dto.oauth.token.OAuthTokenResponse;
import org.kakaoshare.backend.domain.member.dto.oauth.token.RefreshTokenDto;

public record OAuthReissueResponse(String accessToken, RefreshTokenDto refreshToken) {
    public static OAuthReissueResponse from(final OAuthTokenResponse oAuthTokenResponse) {
        return new OAuthReissueResponse(oAuthTokenResponse.access_token(), RefreshTokenDto.from(oAuthTokenResponse));
    }
}
