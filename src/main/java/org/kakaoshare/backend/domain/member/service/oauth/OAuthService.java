package org.kakaoshare.backend.domain.member.service.oauth;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.member.dto.oauth.authenticate.OAuthLoginRequest;
import org.kakaoshare.backend.domain.member.dto.oauth.authenticate.OAuthLoginResponse;
import org.kakaoshare.backend.domain.member.dto.oauth.profile.OAuthProfile;
import org.kakaoshare.backend.domain.member.dto.oauth.profile.OAuthProfileFactory;
import org.kakaoshare.backend.domain.member.dto.oauth.token.OAuthTokenResponse;
import org.kakaoshare.backend.domain.member.entity.MemberDetails;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.jwt.util.JwtProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OAuthService {
    private static final String TOKEN_PREFIX = "Bearer ";

    private final InMemoryClientRegistrationRepository clientRegistrationRepository;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final OAuthWebClientService webClientService;

    @Transactional
    public OAuthLoginResponse login(final OAuthLoginRequest request) {
        final ClientRegistration registration = clientRegistrationRepository.findByRegistrationId(request.provider());
        final OAuthProfile oAuthProfile = getProfile(request, registration);
        return getLoginResponse(oAuthProfile);
    }

    private OAuthProfile getProfile(final OAuthLoginRequest request, final ClientRegistration registration) {
        final Map<String, Object> attributes = webClientService.getSocialProfile(registration, request.code());
        return OAuthProfileFactory.of(attributes, request.provider());
    }

    private Map<String, Object> getAttributes(final OAuthLoginRequest request, final ClientRegistration registration) {
        final OAuthTokenResponse tokenResponse = webClientService.getSocialToken(registration, request.code());
        return webClientService.getSocialProfile(registration, tokenResponse.access_token());
    }

    private OAuthLoginResponse getLoginResponse(final OAuthProfile oAuthProfile) {
        final UserDetails userDetails = addOrFindByProfile(oAuthProfile);
        final String accessToken = jwtProvider.createAccessToken(userDetails.getUsername(), userDetails.getAuthorities());
        return OAuthLoginResponse.of(TOKEN_PREFIX, accessToken);
    }

    private UserDetails addOrFindByProfile(final OAuthProfile oAuthProfile) {
        return memberRepository.findDetailsByProviderId(oAuthProfile.getProviderId())
                .orElseGet(() -> MemberDetails.from(memberRepository.save(oAuthProfile.toEntity())));
    }
}
