package org.kakaoshare.backend.domain.member.dto.oauth.token;

import lombok.Builder;

@Builder
public record OAuthTokenResponse(String access_token, String refresh_token, Long expires_in) {
    public static OAuthTokenResponse of(final String accessToken) {
        return OAuthTokenResponse.builder()
                .access_token(accessToken)
                .build();
    }
}
