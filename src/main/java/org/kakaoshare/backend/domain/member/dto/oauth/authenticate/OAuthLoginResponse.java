package org.kakaoshare.backend.domain.member.dto.oauth.authenticate;

import lombok.Builder;

@Builder
public record OAuthLoginResponse(String accessToken, OAuthLoginMemberResponse member) {
    public static OAuthLoginResponse from(final OAuthLoginResult loginResult) {
        return OAuthLoginResponse.builder()
                .accessToken(loginResult.accessToken())
                .member(loginResult.loginMemberResponse())
                .build();
    }
}