package org.kakaoshare.backend.domain.member.dto.oauth.authenticate;

import lombok.Builder;
import org.kakaoshare.backend.domain.member.dto.oauth.profile.OAuthProfile;
import org.kakaoshare.backend.domain.member.dto.oauth.token.RefreshTokenDto;
import org.kakaoshare.backend.domain.member.entity.token.RefreshToken;

@Builder
public record OAuthLoginResult(String accessToken, RefreshTokenDto refreshToken, OAuthLoginMemberResponse member) {
    public static OAuthLoginResult of(final String accessToken,
                                      final RefreshToken refreshToken,
                                      final OAuthProfile oAuthProfile) {
        return new OAuthLoginResult(accessToken, RefreshTokenDto.from(refreshToken), OAuthLoginMemberResponse.from(oAuthProfile));
    }
}