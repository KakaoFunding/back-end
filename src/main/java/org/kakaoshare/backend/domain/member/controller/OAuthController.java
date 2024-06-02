package org.kakaoshare.backend.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.member.dto.oauth.authenticate.OAuthLoginRequest;
import org.kakaoshare.backend.domain.member.dto.oauth.authenticate.OAuthLoginResponse;
import org.kakaoshare.backend.domain.member.dto.oauth.issue.OAuthReissueRequest;
import org.kakaoshare.backend.domain.member.dto.oauth.issue.ReissueRequest;
import org.kakaoshare.backend.domain.member.dto.oauth.logout.OAuthLogoutRequest;
import org.kakaoshare.backend.domain.member.dto.oauth.logout.OAuthSocialLogoutRequest;
import org.kakaoshare.backend.domain.member.service.oauth.OAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
@RestController
public class OAuthController {
    private final OAuthService oAuthService;
    private final RefreshTokenCookieProvider refreshTokenCookieProvider;

    @PostMapping("/login")
    public ResponseEntity<OAuthLoginResponse> login(@ModelAttribute final OAuthLoginRequest oAuthLoginRequest) {
        return ResponseEntity.ok()
                .body(oAuthService.login(oAuthLoginRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody final OAuthLogoutRequest oAuthLogoutRequest) {
        oAuthService.logout(oAuthLogoutRequest);
        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/social/logout")
    public ResponseEntity<Void> socialLogout(@RequestBody final OAuthSocialLogoutRequest oAuthSocialLogoutRequest) {
        oAuthService.socialLogout(oAuthSocialLogoutRequest);
        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody final ReissueRequest reissueRequest) {
        return ResponseEntity.ok()
                .body(oAuthService.reissue(reissueRequest));
    }

    @PostMapping("/social/reissue")
    public ResponseEntity<?> socialReissue(@RequestBody final OAuthReissueRequest oAuthReissueRequest) {
        return ResponseEntity.ok()
                .body(oAuthService.socialReissue(oAuthReissueRequest));
    }
}
