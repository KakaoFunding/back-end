package org.kakaoshare.backend.domain.member.service.oauth;

import org.kakaoshare.backend.domain.member.dto.oauth.issue.OAuthReissueRequest;
import org.kakaoshare.backend.domain.member.dto.oauth.logout.OAuthSocialLogoutRequest;
import org.kakaoshare.backend.domain.member.dto.oauth.token.OAuthTokenResponse;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

import java.util.Map;

public interface OAuthWebClientService {
    Map<String, Object> getSocialProfile(final ClientRegistration registration, final String socialToken);
    OAuthTokenResponse issueToken(final ClientRegistration registration, final OAuthReissueRequest oAuthReissueRequest);
    void expireToken(final ClientRegistration registration, final OAuthSocialLogoutRequest oAuthSocialLogoutRequest);
}
