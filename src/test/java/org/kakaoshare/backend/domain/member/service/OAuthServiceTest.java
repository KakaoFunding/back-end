package org.kakaoshare.backend.domain.member.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kakaoshare.backend.domain.member.dto.oauth.authenticate.OAuthLoginRequest;
import org.kakaoshare.backend.domain.member.dto.oauth.authenticate.OAuthLoginResult;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kakaoshare.backend.fixture.MemberFixture.KAKAO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

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
        doReturn(Optional.empty()).when(memberRepository).findDetailsByProviderId(providerId);
        doReturn(member).when(memberRepository).save(any());
        doReturn(accessToken).when(jwtProvider).createAccessToken(userDetails);
        doReturn(refreshToken).when(refreshTokenProvider).createToken(userDetails.getUsername());
        doReturn(refreshToken).when(refreshTokenRepository).save(any());

        final OAuthLoginResult expect = OAuthLoginResult.of(accessToken, refreshToken.getValue(), oAuthProfile);
        final OAuthLoginResult actual = oAuthService.login(request);
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
        doReturn(Optional.of(userDetails)).when(memberRepository).findDetailsByProviderId(providerId);
        doReturn(accessToken).when(jwtProvider).createAccessToken(userDetails);
        doReturn(refreshToken).when(refreshTokenProvider).createToken(userDetails.getUsername());
        doReturn(refreshToken).when(refreshTokenRepository).save(any());

        final OAuthLoginResult expect = OAuthLoginResult.of(accessToken, refreshToken.getValue(), oAuthProfile);
        final OAuthLoginResult actual = oAuthService.login(request);
        assertThat(expect).isEqualTo(actual);
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