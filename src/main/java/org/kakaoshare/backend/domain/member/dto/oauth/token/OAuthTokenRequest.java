package org.kakaoshare.backend.domain.member.dto.oauth.token;

import lombok.Builder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

@Builder
public record OAuthTokenRequest(String code, String client_id, String client_secret, String grant_type, String redirect_id) {
    public static OAuthTokenRequest of(final ClientRegistration registration,
                                       final String code) {
        return OAuthTokenRequest.builder()
                .code(code)
                .client_id(registration.getClientId())
                .client_secret(registration.getClientSecret())
                .grant_type(registration.getAuthorizationGrantType().getValue())
                .redirect_id(registration.getRedirectUri())
                .build();
    }
}