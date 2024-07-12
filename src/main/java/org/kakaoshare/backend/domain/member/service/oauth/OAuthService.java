package org.kakaoshare.backend.domain.member.service.oauth;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.member.dto.oauth.authenticate.OAuthLoginRequest;
import org.kakaoshare.backend.domain.member.dto.oauth.authenticate.OAuthLoginResponse;
import org.kakaoshare.backend.domain.member.dto.oauth.issue.OAuthReissueRequest;
import org.kakaoshare.backend.domain.member.dto.oauth.issue.OAuthReissueResponse;
import org.kakaoshare.backend.domain.member.dto.oauth.issue.ReissueResult;
import org.kakaoshare.backend.domain.member.dto.oauth.logout.OAuthSocialLogoutRequest;
import org.kakaoshare.backend.domain.member.dto.oauth.profile.OAuthProfile;
import org.kakaoshare.backend.domain.member.dto.oauth.profile.OAuthProfileFactory;
import org.kakaoshare.backend.domain.member.dto.oauth.token.OAuthTokenResponse;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.entity.MemberDetails;
import org.kakaoshare.backend.domain.member.entity.token.RefreshToken;
import org.kakaoshare.backend.domain.member.exception.MemberErrorCode;
import org.kakaoshare.backend.domain.member.exception.MemberException;
import org.kakaoshare.backend.domain.member.exception.token.RefreshTokenErrorCode;
import org.kakaoshare.backend.domain.member.exception.token.RefreshTokenException;
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
import java.util.Optional;

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
    public OAuthLoginResponse login(final OAuthLoginRequest oAuthLoginRequest) {
        final ClientRegistration registration = clientRegistrationRepository.findByRegistrationId(oAuthLoginRequest.provider());
        final OAuthProfile oAuthProfile = getProfile(oAuthLoginRequest, registration);
        final UserDetails userDetails = addOrFindByProfile(oAuthProfile);
        final String accessToken = jwtProvider.createAccessToken(userDetails);
        final RefreshToken refreshToken = refreshTokenProvider.createToken(userDetails.getUsername());
        refreshTokenRepository.save(refreshToken);

        return OAuthLoginResponse.of(accessToken, refreshToken, oAuthProfile);
    }

    @Transactional
    public void logout(final String refreshTokenValue) {
        final RefreshToken refreshToken = findRefreshTokenByValue(refreshTokenValue);
        refreshTokenRepository.delete(refreshToken);
    }

    public void socialLogout(final OAuthSocialLogoutRequest oAuthSocialLogoutRequest) {
        final String provider = oAuthSocialLogoutRequest.provider();
        final ClientRegistration registration = clientRegistrationRepository.findByRegistrationId(provider);
        webClientService.expireToken(registration, oAuthSocialLogoutRequest);
    }

    @Transactional
    public ReissueResult reissue(final String refreshTokenValue) {
        final RefreshToken refreshToken = findRefreshTokenByValue(refreshTokenValue);
        final String providerId = refreshToken.getProviderId();
        final UserDetails userDetails = findUserDetailsByProviderId(providerId);
        final String accessToken = jwtProvider.createAccessToken(userDetails);
        final RefreshToken newRefreshToken = refreshTokenProvider.createToken(providerId);
        refreshTokenRepository.delete(refreshToken);
        refreshTokenRepository.save(newRefreshToken);

        return ReissueResult.of(accessToken, newRefreshToken);
    }

    public OAuthReissueResponse socialReissue(final OAuthReissueRequest oAuthReissueRequest) {
        final String provider = oAuthReissueRequest.provider();
        final ClientRegistration registration = clientRegistrationRepository.findByRegistrationId(provider);
        final OAuthTokenResponse oAuthTokenResponse = webClientService.issueToken(registration, oAuthReissueRequest);
        return OAuthReissueResponse.from(oAuthTokenResponse);   // TODO: 5/27/24 응답 중 refresh_token 값은 요청 시 사용된 리프레시 토큰의 만료 시간이 1개월 미만으로 남았을 때만 갱신되어 전달
    }

    private OAuthProfile getProfile(final OAuthLoginRequest request, final ClientRegistration registration) {
        final Map<String, Object> attributes = webClientService.getSocialProfile(registration, request.socialAccessToken());
        return OAuthProfileFactory.of(attributes, request.provider());
    }

    private UserDetails addOrFindByProfile(final OAuthProfile oAuthProfile) {
        final String providerId = oAuthProfile.getProviderId();
        final Optional<Member> optionalMember = memberRepository.findMemberByProviderId(providerId);
        if (optionalMember.isEmpty()) {
            final Member member = memberRepository.save(oAuthProfile.toEntity());
            return MemberDetails.from(member);
        }

        final Member member = optionalMember.get();
        member.updateProfileUrl(oAuthProfile.getProfileImageUrl());
        return MemberDetails.from(member);
    }

    private UserDetails findUserDetailsByProviderId(final String providerId) {
        return memberRepository.findDetailsByProviderId(providerId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));
    }

    private RefreshToken findRefreshTokenByValue(final String refreshTokenValue) {
        return refreshTokenRepository.findByValue(refreshTokenValue)
                .orElseThrow(() -> new RefreshTokenException(RefreshTokenErrorCode.NOT_FOUND));
    }
}
