package org.kakaoshare.backend.domain.member.controller;

import com.querydsl.core.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.member.dto.oauth.authenticate.OAuthLoginRequest;
import org.kakaoshare.backend.domain.member.dto.oauth.authenticate.OAuthLoginResponse;
import org.kakaoshare.backend.domain.member.dto.oauth.authenticate.OAuthLoginResult;
import org.kakaoshare.backend.domain.member.dto.oauth.issue.IssuedTokenResponse;
import org.kakaoshare.backend.domain.member.dto.oauth.issue.IssuedTokenResult;
import org.kakaoshare.backend.domain.member.exception.token.RefreshTokenException;
import org.kakaoshare.backend.domain.member.service.oauth.OAuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.kakaoshare.backend.domain.member.controller.RefreshTokenCookieProvider.REFRESH_TOKEN;
import static org.kakaoshare.backend.domain.member.exception.token.RefreshTokenErrorCode.NOT_FOUND;

@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
@RestController
public class OAuthController {
    private final OAuthService oAuthService;
    private final RefreshTokenCookieProvider refreshTokenCookieProvider;

    @PostMapping("/login")
    public ResponseEntity<OAuthLoginResponse> login(@ModelAttribute final OAuthLoginRequest oAuthLoginRequest) {
        final OAuthLoginResult loginResult = oAuthService.login(oAuthLoginRequest);
        final String refreshToken = loginResult.refreshToken();
        final ResponseCookie cookie = refreshTokenCookieProvider.createCookie(refreshToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(OAuthLoginResponse.from(loginResult));
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(value = REFRESH_TOKEN, required = false) final String refreshTokenValue) {
        validateRefreshTokenExists(refreshTokenValue);

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookieProvider.createLogoutCookie().toString())
                .build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(
            @CookieValue(value = REFRESH_TOKEN, required = false) final String refreshTokenValue) {
        validateRefreshTokenExists(refreshTokenValue);
        final IssuedTokenResult issuedTokenResult = oAuthService.reissue(refreshTokenValue);
        final ResponseCookie cookie = refreshTokenCookieProvider.createCookie(issuedTokenResult.refreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(IssuedTokenResponse.from(issuedTokenResult));
    }

    private void validateRefreshTokenExists(final String refreshToken) {
        if (StringUtils.isNullOrEmpty(refreshToken)) {
            throw new RefreshTokenException(NOT_FOUND);
        }
    }
}
