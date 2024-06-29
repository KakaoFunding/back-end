package org.kakaoshare.backend.domain.member.controller;

import org.apache.logging.log4j.util.Strings;
import org.kakaoshare.backend.domain.member.dto.oauth.token.RefreshTokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import static org.springframework.http.ResponseCookie.ResponseCookieBuilder;

@Component
public class RefreshTokenCookieProvider {
    protected static final String REFRESH_TOKEN = "refreshToken";
    private static final String ALL_PATH = "/";
    private static final int REMOVAL_MAX_AGE = 0;

    private final Long expireTime;

    public RefreshTokenCookieProvider(@Value("${security.token.refresh.expire-time}") final Long expireTime) {
        this.expireTime = expireTime;
    }

    public ResponseCookie createCookie(final RefreshTokenDto refreshToken) {
        return createTokenCookieBuilder(refreshToken.value())
                .maxAge(expireTime)
                .build();
    }

    public ResponseCookie createLogoutCookie() {
        return createTokenCookieBuilder(Strings.EMPTY)
                .maxAge(REMOVAL_MAX_AGE)
                .build();
    }

    private ResponseCookieBuilder createTokenCookieBuilder(final String value) {
        return ResponseCookie.from(REFRESH_TOKEN, value)
                .httpOnly(true)
                .secure(true)
                .path(ALL_PATH)
                .sameSite(Cookie.SameSite.NONE.attributeValue());
    }
}
