package org.kakaoshare.backend.domain.member.service.oauth;

import org.kakaoshare.backend.domain.member.dto.oauth.logout.OAuthSocialLogoutRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

import java.util.Map;

public interface OAuthWebClientService {
    Map<String, Object> getSocialProfile(final ClientRegistration registration, final String socialToken);

    void expireToken(final ClientRegistration registration, final OAuthSocialLogoutRequest oAuthSocialLogoutRequest);
}
