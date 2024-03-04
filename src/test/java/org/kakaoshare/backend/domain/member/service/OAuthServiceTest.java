package org.kakaoshare.backend.domain.member.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kakaoshare.backend.domain.member.dto.oauth.authenticate.OAuthLoginRequest;
import org.kakaoshare.backend.domain.member.dto.oauth.authenticate.OAuthLoginResponse;
import org.kakaoshare.backend.domain.member.dto.oauth.token.OAuthTokenResponse;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.entity.MemberDetails;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.domain.member.service.oauth.OAuthService;
import org.kakaoshare.backend.domain.member.service.oauth.OAuthWebClientService;
import org.kakaoshare.backend.jwt.util.JwtProvider;
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

    @InjectMocks
    OAuthService oAuthService;

    private String accessToken;
    private String providerId;
    private OAuthTokenResponse oAuthTokenResponse;
    private Member member;
    private UserDetails userDetails;
    private String code;
    private String grantType;

    @BeforeEach
    public void setUp() {
        code = "code";
        grantType = "Bearer ";
        accessToken = "accessToken";
        oAuthTokenResponse = getOAuthTokenResponse();
        member = KAKAO.생성();
        providerId = member.getProviderId();
        userDetails = MemberDetails.from(member);
    }

    @Test
    @DisplayName("신규 회원 카카오 로그인")
    public void authenticateWhenNewMember() throws Exception {
        final String registrationId = "kakao";
        final ClientRegistration registration = getClientRegistration(registrationId);
        final Map<String, Object> attributes = kakaoAttributes();
        final OAuthLoginRequest request = new OAuthLoginRequest(registrationId, code);

        doReturn(registration).when(clientRegistrationRepository).findByRegistrationId(registrationId);
        doReturn(oAuthTokenResponse).when(webClientService).getSocialToken(registration, request.code());
        doReturn(attributes).when(webClientService).getSocialProfile(registration, oAuthTokenResponse.access_token());
        doReturn(Optional.empty()).when(memberRepository).findDetailsByProviderId(providerId);
        doReturn(member).when(memberRepository).save(any());
        doReturn(accessToken).when(jwtProvider).createAccessToken(userDetails.getUsername(), userDetails.getAuthorities());

        final OAuthLoginResponse expect = OAuthLoginResponse.of(grantType, accessToken);
        final OAuthLoginResponse actual = oAuthService.login(request);
        assertThat(expect).usingRecursiveComparison()
                .isEqualTo(actual);
    }

    @Test
    @DisplayName("기존 회원 카카오 로그인")
    public void authenticateWhenExistingMember() throws Exception {
        final String registrationId = "kakao";
        final ClientRegistration registration = getClientRegistration(registrationId);
        final Map<String, Object> attributes = kakaoAttributes();
        final OAuthLoginRequest request = new OAuthLoginRequest(registrationId, code);

        doReturn(registration).when(clientRegistrationRepository).findByRegistrationId(registrationId);
        doReturn(oAuthTokenResponse).when(webClientService).getSocialToken(registration, request.code());
        doReturn(attributes).when(webClientService).getSocialProfile(registration, oAuthTokenResponse.access_token());
        doReturn(Optional.of(userDetails)).when(memberRepository).findDetailsByProviderId(providerId);
        doReturn(accessToken).when(jwtProvider).createAccessToken(userDetails.getUsername(), userDetails.getAuthorities());

        final OAuthLoginResponse expect = OAuthLoginResponse.of(grantType, accessToken);
        final OAuthLoginResponse actual = oAuthService.login(request);
        assertThat(expect).usingRecursiveComparison()
                .isEqualTo(actual);
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

    private OAuthTokenResponse getOAuthTokenResponse() {
        return OAuthTokenResponse.builder()
                .access_token("1234")
                .refresh_token("5678")
                .expires_in(100L)
                .build();
    }

    private Map<String, Object> kakaoAttributes() {
        final Map<String, Object> profile = new HashMap<>();
        profile.put("nickname", "테스터");

        final Map<String, Object> account = new HashMap<>();
        account.put("profile", profile);
        account.put("gender", "MALE");

        final Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", providerId);
        attributes.put("kakao_account", account);

        return attributes;
    }
}