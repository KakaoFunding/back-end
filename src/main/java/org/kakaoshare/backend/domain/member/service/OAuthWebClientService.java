package org.kakaoshare.backend.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.member.dto.oauth.token.OAuthTokenRequest;
import org.kakaoshare.backend.domain.member.dto.oauth.token.OAuthTokenResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class OAuthWebClientService {
    private final WebClient webClient;

    public OAuthTokenResponse getSocialToken(final ClientRegistration registration,
                                             final String code) {
        return webClient.post()
                .uri(getSocialTokenRequestUri(registration))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .bodyValue(OAuthTokenRequest.of(registration, code))
                .retrieve()
                .bodyToMono(OAuthTokenResponse.class)
                .block();
    }

    public Map<String, Object> getSocialProfile(final ClientRegistration registration,
                                                final String socialToken) {
        return webClient.get()
                .uri(getProfileRequestUri(registration))
                .headers(header -> header.setBearerAuth(socialToken))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();

    }

    private String getSocialTokenRequestUri(final ClientRegistration registration) {
        return registration.getAuthorizationGrantType()
                .getValue();
    }

    private String getProfileRequestUri(final ClientRegistration registration) {
        return registration.getProviderDetails()
                .getUserInfoEndpoint()
                .getUri();
    }
}
