package org.kakaoshare.backend.domain.member.service.oauth.detail.kakao;

import org.kakaoshare.backend.common.error.kakao.handler.KakaoErrorHandler;
import org.kakaoshare.backend.common.util.MultiValueMapConverter;
import org.kakaoshare.backend.domain.member.dto.oauth.issue.OAuthReissueRequest;
import org.kakaoshare.backend.domain.member.dto.oauth.logout.OAuthSocialLogoutRequest;
import org.kakaoshare.backend.domain.member.dto.oauth.logout.detail.kakao.request.KakaoLogoutRequest;
import org.kakaoshare.backend.domain.member.dto.oauth.token.OAuthTokenRequest;
import org.kakaoshare.backend.domain.member.dto.oauth.token.OAuthTokenResponse;
import org.kakaoshare.backend.domain.member.service.oauth.OAuthWebClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class KakaoOAuthService implements OAuthWebClientService {
    private final KakaoErrorHandler kakaoErrorHandler;
    private final KakaoOAuthRequestProvider kakaoOAuthRequestProvider;
    private final String logoutRequestUrl;
    private final WebClient webClient;

    public KakaoOAuthService(final KakaoErrorHandler kakaoErrorHandler,
                             final KakaoOAuthRequestProvider kakaoOAuthRequestProvider,
                             @Value("${spring.security.oauth2.client.other.kakao.logout-url}") final String logoutRequestUrl,
                             final WebClient webClient) {
        this.kakaoErrorHandler = kakaoErrorHandler;
        this.kakaoOAuthRequestProvider = kakaoOAuthRequestProvider;
        this.logoutRequestUrl = logoutRequestUrl;
        this.webClient = webClient;
    }

    @Override
    public Map<String, Object> getSocialProfile(final ClientRegistration registration,
                                                final String socialToken) {
        return webClient.get()
                .uri(getProfileRequestUri(registration))
                .headers(header -> header.setBearerAuth(socialToken))
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, kakaoErrorHandler::handle5xxError)
                .onStatus(HttpStatusCode::is4xxClientError, kakaoErrorHandler::handleApi4xxError)
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    @Override
    public OAuthTokenResponse issueToken(final ClientRegistration registration,
                                         final OAuthReissueRequest oAuthReissueRequest) {
        final String socialRefreshToken = oAuthReissueRequest.refreshToken();
        final OAuthTokenRequest oAuthTokenRequest = OAuthTokenRequest.of(registration, socialRefreshToken);
        final MultiValueMap<String, String> params = MultiValueMapConverter.convert(oAuthTokenRequest);
        return webClient.post()
                .uri(getTokenRequestUri(registration))
                .headers(header -> header.setContentType(MediaType.APPLICATION_FORM_URLENCODED))
                .body(BodyInserters.fromFormData(params))
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, kakaoErrorHandler::handle5xxError)
                .onStatus(HttpStatusCode::is4xxClientError, kakaoErrorHandler::handleAuth4xxError)
                .bodyToMono(OAuthTokenResponse.class)
                .block();
    }

    @Override
    public void expireToken(final ClientRegistration registration,
                            final OAuthSocialLogoutRequest oAuthSocialLogoutRequest) {
        final String socialAccessTokenToken = oAuthSocialLogoutRequest.socialAccessToken();
        final String providerId = oAuthSocialLogoutRequest.providerId();
        final KakaoLogoutRequest kakaoLogoutRequest = kakaoOAuthRequestProvider.createKakaoLogoutRequest(providerId);
        webClient.post()
                .uri(logoutRequestUrl)
                .headers(header -> header.setBearerAuth(socialAccessTokenToken))
                .bodyValue(kakaoLogoutRequest)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, kakaoErrorHandler::handle5xxError)
                .onStatus(HttpStatusCode::is4xxClientError, kakaoErrorHandler::handleApi4xxError)
                .bodyToMono(Void.class)
                .block();
    }

    private String getProfileRequestUri(final ClientRegistration registration) {
        return registration.getProviderDetails()
                .getUserInfoEndpoint()
                .getUri();
    }

    private String getTokenRequestUri(final ClientRegistration registration) {
        return registration.getProviderDetails()
                .getTokenUri();
    }
}
