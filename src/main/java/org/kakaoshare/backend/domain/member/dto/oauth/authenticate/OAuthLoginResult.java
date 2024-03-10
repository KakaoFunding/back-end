package org.kakaoshare.backend.domain.member.dto.oauth.authenticate;

import org.kakaoshare.backend.domain.member.dto.oauth.profile.OAuthProfile;

public record OAuthLoginResult(String accessToken, String refreshToken, OAuthLoginMemberResponse loginMemberResponse) {
    public static OAuthLoginResult of(final String accessToken,
                                      final String refreshToken,
                                      final OAuthProfile oAuthProfile) {
        return new OAuthLoginResult(accessToken, refreshToken, OAuthLoginMemberResponse.from(oAuthProfile));
    }
}
