package org.kakaoshare.backend.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.member.dto.oauth.authenticate.OAuthLoginRequest;
import org.kakaoshare.backend.domain.member.dto.oauth.authenticate.OAuthLoginResponse;
import org.kakaoshare.backend.domain.member.service.oauth.OAuthService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
@RestController
public class OAuthController {
    private final OAuthService oAuthService;

    @PostMapping("/login")
    public OAuthLoginResponse login(@ModelAttribute final OAuthLoginRequest oAuthAuthenticateRequest) {
        return oAuthService.login(oAuthAuthenticateRequest);
    }
}
