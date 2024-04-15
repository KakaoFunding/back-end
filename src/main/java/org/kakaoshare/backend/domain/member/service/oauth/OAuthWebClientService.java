package org.kakaoshare.backend.domain.member.service.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class OAuthWebClientService {
    private final WebClient webClient;

    public Map<String, Object> getSocialProfile(final ClientRegistration registration,
                                                final String socialToken) {
        return webClient.get()
                .uri(getProfileRequestUri(registration))
                .headers(header -> header.setBearerAuth(socialToken))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    private String getProfileRequestUri(final ClientRegistration registration) {
        return registration.getProviderDetails()
                .getUserInfoEndpoint()
                .getUri();
    }
}
