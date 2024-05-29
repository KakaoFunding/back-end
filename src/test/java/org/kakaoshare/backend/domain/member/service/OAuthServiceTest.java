package org.kakaoshare.backend.domain.member.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kakaoshare.backend.domain.member.dto.oauth.authenticate.OAuthLoginRequest;
import org.kakaoshare.backend.domain.member.dto.oauth.authenticate.OAuthLoginResponse;
import org.kakaoshare.backend.domain.member.dto.oauth.issue.OAuthReissueRequest;
import org.kakaoshare.backend.domain.member.dto.oauth.issue.OAuthReissueResponse;
import org.kakaoshare.backend.domain.member.dto.oauth.logout.OAuthSocialLogoutRequest;
import org.kakaoshare.backend.domain.member.dto.oauth.profile.OAuthProfile;
import org.kakaoshare.backend.domain.member.dto.oauth.profile.OAuthProfileFactory;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.entity.MemberDetails;
import org.kakaoshare.backend.domain.member.entity.token.RefreshToken;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.domain.member.repository.token.RefreshTokenRepository;
import org.kakaoshare.backend.domain.member.service.oauth.OAuthService;
import org.kakaoshare.backend.domain.member.service.oauth.OAuthWebClientService;
import org.kakaoshare.backend.jwt.util.JwtProvider;
import org.kakaoshare.backend.jwt.util.RefreshTokenProvider;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.kakaoshare.backend.fixture.MemberFixture.KAKAO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OAuthServiceTest {
    @Mock
    InMemoryClientRegistrationRepository clientRegistrationRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    JwtProvider jwtProvider;

    @Mock
    OAuthWebClientService webClientService;

    @Mock
    RefreshTokenProvider refreshTokenProvider;

    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    OAuthService oAuthService;

    private String accessToken;
    private String socialAccessToken;
    private Member member;
    private String providerId;
    private RefreshToken refreshToken;
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        accessToken = "accessToken";
        socialAccessToken = "socialAccessToken";
        member = KAKAO.생성();
        providerId = member.getProviderId();
        refreshToken = getRefreshToken();
        userDetails = MemberDetails.from(member);
    }

    @Test
    @DisplayName("신규 회원 카카오 로그인")
    public void authenticateWhenNewMember() throws Exception {
        final String registrationId = "kakao";
        final ClientRegistration registration = getClientRegistration(registrationId);
        final Map<String, Object> attributes = kakaoAttributes();
        final OAuthProfile oAuthProfile = OAuthProfileFactory.of(attributes, registrationId);
        final OAuthLoginRequest request = new OAuthLoginRequest(registrationId, socialAccessToken);

        doReturn(registration).when(clientRegistrationRepository).findByRegistrationId(registrationId);
        doReturn(attributes).when(webClientService).getSocialProfile(registration, socialAccessToken);
        doReturn(member).when(memberRepository).save(any());
        doReturn(accessToken).when(jwtProvider).createAccessToken(userDetails);
        doReturn(refreshToken).when(refreshTokenProvider).createToken(userDetails.getUsername());
        doReturn(refreshToken).when(refreshTokenRepository).save(any());

        final OAuthLoginResponse expect = OAuthLoginResponse.of(accessToken, refreshToken, oAuthProfile);
        final OAuthLoginResponse actual = oAuthService.login(request);
        assertThat(expect).isEqualTo(actual);
    }

    @Test
    @DisplayName("기존 회원 카카오 로그인")
    public void authenticateWhenExistingMember() throws Exception {
        final String registrationId = "kakao";
        final ClientRegistration registration = getClientRegistration(registrationId);
        final Map<String, Object> attributes = kakaoAttributes();
        final OAuthProfile oAuthProfile = OAuthProfileFactory.of(attributes, registrationId);
        final OAuthLoginRequest request = new OAuthLoginRequest(registrationId, socialAccessToken);

        doReturn(registration).when(clientRegistrationRepository).findByRegistrationId(registrationId);
        doReturn(attributes).when(webClientService).getSocialProfile(registration, socialAccessToken);
        doReturn(member).when(memberRepository).save(any());
        doReturn(accessToken).when(jwtProvider).createAccessToken(userDetails);
        doReturn(refreshToken).when(refreshTokenProvider).createToken(userDetails.getUsername());
        doReturn(refreshToken).when(refreshTokenRepository).save(any());

        final OAuthLoginResponse expect = OAuthLoginResponse.of(accessToken, refreshToken, oAuthProfile);
        final OAuthLoginResponse actual = oAuthService.login(request);
        assertThat(expect).isEqualTo(actual);
    }

    @Test
    @DisplayName("토큰 재발급")
    public void reissue() throws Exception {
        final String refreshTokenValue = refreshToken.getValue();
        final RefreshToken newRefreshToken = RefreshToken.from(providerId, "newRefreshToken", 100L);

        doReturn(Optional.of(refreshToken)).when(refreshTokenRepository).findByValue(refreshTokenValue);
        doReturn(Optional.of(userDetails)).when(memberRepository).findDetailsByProviderId(providerId);
        doReturn(accessToken).when(jwtProvider).createAccessToken(userDetails);
        doReturn(newRefreshToken).when(refreshTokenProvider).createToken(providerId);
        doReturn(newRefreshToken).when(refreshTokenRepository).save(newRefreshToken);

        final OAuthReissueRequest oAuthReissueRequest = new OAuthReissueRequest(refreshTokenValue);
        final OAuthReissueResponse actual = oAuthService.reissue(oAuthReissueRequest);
        final OAuthReissueResponse expect = OAuthReissueResponse.of(accessToken, newRefreshToken);

        assertThat(actual).isEqualTo(expect);
    }

    @Test
    @DisplayName("카카오 소셜 로그아웃")
    public void socialLogout() throws Exception {
        final String provider = "kakao";
        final ClientRegistration registration = getClientRegistration(provider);
        final OAuthSocialLogoutRequest oAuthSocialLogoutRequest = new OAuthSocialLogoutRequest(provider, providerId, socialAccessToken);

        doReturn(registration).when(clientRegistrationRepository).findByRegistrationId(provider);
        doNothing().when(webClientService).expireToken(registration, oAuthSocialLogoutRequest);
        assertThatCode(() -> oAuthService.socialLogout(oAuthSocialLogoutRequest))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("카카오 소셜 로그아웃에서 providerId가 잘못되면 예외가 발생")
    public void socialLogoutWhenIvalidProviderId() throws Exception {
        final String provider = "kakao";
        final ClientRegistration registration = getClientRegistration(provider);
        final OAuthSocialLogoutRequest oAuthSocialLogoutRequest = new OAuthSocialLogoutRequest(provider, providerId, socialAccessToken);

        doReturn(registration).when(clientRegistrationRepository).findByRegistrationId(provider);
        doThrow(WebClientResponseException.class).when(webClientService).expireToken(registration, oAuthSocialLogoutRequest);
        assertThatThrownBy(() -> oAuthService.socialLogout(oAuthSocialLogoutRequest))
                .isInstanceOf(WebClientResponseException.class);
    }

    private ClientRegistration getClientRegistration(final String registrationId) {
        return ClientRegistration.withRegistrationId(registrationId)
                .clientId("client-id")
                .clientSecret("client-secret")
                .clientName("client-name")
                .authorizationUri("authorization-uri")
                .authorizationGrantType(new AuthorizationGrantType("authorization_code"))
                .redirectUri("redirect-uri")
                .tokenUri("token-uri")
                .scope(Set.of("profile", "email"))
                .build();
    }

    private RefreshToken getRefreshToken() {
        return RefreshToken.from(providerId, "refreshToken", 100L);
    }

    private Map<String, Object> kakaoAttributes() {
        final Map<String, Object> profile = new HashMap<>();
        profile.put("name", "테스터");

        final Map<String, Object> account = new HashMap<>();
        account.put("profile", profile);
        account.put("gender", "MALE");

        final Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", providerId);
        attributes.put("kakao_account", account);

        return attributes;
    }
}