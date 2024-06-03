package org.kakaoshare.backend.domain.member.dto.oauth.token;

import lombok.Builder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

@Builder
public record OAuthTokenRequest(String grant_type, String client_id, String refresh_token, String client_secret) {
    public static OAuthTokenRequest of(final ClientRegistration registration, final String refreshToken) {
        return OAuthTokenRequest.builder()
                .grant_type(registration.getAuthorizationGrantType().getValue())
                .client_id(registration.getClientId())
                .refresh_token(refreshToken)
                .client_secret(registration.getClientSecret())
                .build();
    }
}