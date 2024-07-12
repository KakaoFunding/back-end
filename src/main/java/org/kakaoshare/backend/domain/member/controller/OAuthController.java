package org.kakaoshare.backend.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.member.dto.oauth.authenticate.OAuthLoginRequest;
import org.kakaoshare.backend.domain.member.dto.oauth.authenticate.OAuthLoginResponse;
import org.kakaoshare.backend.domain.member.dto.oauth.issue.OAuthReissueRequest;
import org.kakaoshare.backend.domain.member.dto.oauth.issue.ReissueResponse;
import org.kakaoshare.backend.domain.member.dto.oauth.issue.ReissueResult;
import org.kakaoshare.backend.domain.member.dto.oauth.logout.OAuthSocialLogoutRequest;
import org.kakaoshare.backend.domain.member.service.oauth.OAuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.kakaoshare.backend.domain.member.controller.RefreshTokenCookieProvider.REFRESH_TOKEN;

@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
@RestController
public class OAuthController {
    private final OAuthService oAuthService;
    private final RefreshTokenCookieProvider refreshTokenCookieProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid final OAuthLoginRequest oAuthLoginRequest) {
        final OAuthLoginResponse loginResponse = oAuthService.login(oAuthLoginRequest);
        final ResponseCookie cookie = refreshTokenCookieProvider.createCookie(loginResponse.refreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @CookieValue(value = REFRESH_TOKEN, required = false) final String refreshTokenValue
    ) {
        oAuthService.logout(refreshTokenValue);
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookieProvider.createLogoutCookie().toString())
                .build();
    }

    @PostMapping("/social/logout")
    public ResponseEntity<?> socialLogout(@RequestBody final OAuthSocialLogoutRequest oAuthSocialLogoutRequest) {
        oAuthService.socialLogout(oAuthSocialLogoutRequest);
        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(
            @CookieValue(value = REFRESH_TOKEN, required = false) final String refreshTokenValue
    ) {
        final ReissueResult reissueResult = oAuthService.reissue(refreshTokenValue);
        final ResponseCookie cookie = refreshTokenCookieProvider.createCookie(reissueResult.refreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ReissueResponse.from(reissueResult));
    }

    @PostMapping("/social/reissue")
    public ResponseEntity<?> socialReissue(@RequestBody final OAuthReissueRequest oAuthReissueRequest) {
        return ResponseEntity.ok()
                .body(oAuthService.socialReissue(oAuthReissueRequest));
    }
}
