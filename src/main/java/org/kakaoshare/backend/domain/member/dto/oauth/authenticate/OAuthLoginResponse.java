package org.kakaoshare.backend.domain.member.dto.oauth.authenticate;

public record OAuthLoginResponse(String accessToken, OAuthLoginMemberResponse member) {
    public static OAuthLoginResponse from(final OAuthLoginResult oAuthLoginResult) {
        return new OAuthLoginResponse(oAuthLoginResult.accessToken(), oAuthLoginResult.member());
    }
}
