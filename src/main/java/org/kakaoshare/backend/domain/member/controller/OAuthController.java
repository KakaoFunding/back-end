package org.kakaoshare.backend.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.member.dto.oauth.authenticate.OAuthLoginRequest;
import org.kakaoshare.backend.domain.member.dto.oauth.authenticate.OAuthLoginResponse;
import org.kakaoshare.backend.domain.member.dto.oauth.authenticate.OAuthLoginResult;
import org.kakaoshare.backend.domain.member.service.oauth.OAuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
