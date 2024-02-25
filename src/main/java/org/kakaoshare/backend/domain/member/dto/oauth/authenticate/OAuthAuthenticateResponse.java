package org.kakaoshare.backend.domain.member.dto.oauth.authenticate;

import lombok.Builder;

@Builder
public record OAuthAuthenticateResponse(String grantType, String accessToken) {
    public static OAuthAuthenticateResponse of(final String grantType,
                                               final String accessToken) {
        return OAuthAuthenticateResponse.builder()
                .grantType(grantType)
                .accessToken(accessToken)
                .build();
    }
}