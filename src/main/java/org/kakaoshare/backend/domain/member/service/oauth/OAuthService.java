package org.kakaoshare.backend.domain.member.service.oauth;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.member.dto.oauth.authenticate.OAuthLoginRequest;
import org.kakaoshare.backend.domain.member.dto.oauth.authenticate.OAuthLoginResult;
import org.kakaoshare.backend.domain.member.dto.oauth.profile.OAuthProfile;
import org.kakaoshare.backend.domain.member.dto.oauth.profile.OAuthProfileFactory;
import org.kakaoshare.backend.domain.member.entity.MemberDetails;
import org.kakaoshare.backend.domain.member.entity.token.RefreshToken;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.domain.member.repository.token.RefreshTokenRepository;
import org.kakaoshare.backend.jwt.util.JwtProvider;
import org.kakaoshare.backend.jwt.util.RefreshTokenProvider;
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
    private final InMemoryClientRegistrationRepository clientRegistrationRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenProvider refreshTokenProvider;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuthWebClientService webClientService;

    @Transactional
    public OAuthLoginResult login(final OAuthLoginRequest oAuthLoginRequest) {
        final ClientRegistration registration = clientRegistrationRepository.findByRegistrationId(oAuthLoginRequest.provider());
        final OAuthProfile oAuthProfile = getProfile(oAuthLoginRequest, registration);
        final UserDetails userDetails = addOrFindByProfile(oAuthProfile);
        final String accessToken = jwtProvider.createAccessToken(userDetails);
        final RefreshToken refreshToken = refreshTokenProvider.createToken(userDetails.getUsername());
        refreshTokenRepository.save(refreshToken);

        return OAuthLoginResult.of(accessToken, refreshToken.getValue(), oAuthProfile);
    }

    private OAuthProfile getProfile(final OAuthLoginRequest request, final ClientRegistration registration) {
        final Map<String, Object> attributes = webClientService.getSocialProfile(registration, request.socialAccessToken());
        return OAuthProfileFactory.of(attributes, request.provider());
    }

    private UserDetails addOrFindByProfile(final OAuthProfile oAuthProfile) {
        return memberRepository.findDetailsByProviderId(oAuthProfile.getProviderId())
                .orElseGet(() -> MemberDetails.from(memberRepository.save(oAuthProfile.toEntity())));
    }
}
