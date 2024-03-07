package org.kakaoshare.backend.domain.member.dto.oauth.authenticate;

import lombok.Builder;

@Builder
public record OAuthLoginResponse(String grantType, String accessToken) {
    public static OAuthLoginResponse of(final String grantType,
                                        final String accessToken) {
        return OAuthLoginResponse.builder()
                .grantType(grantType)
                .accessToken(accessToken)
                .build();
    }
}