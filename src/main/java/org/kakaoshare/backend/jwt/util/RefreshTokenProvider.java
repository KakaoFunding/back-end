package org.kakaoshare.backend.jwt.util;

import org.kakaoshare.backend.domain.member.entity.token.RefreshToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RefreshTokenProvider {
    private final long expireTime;

    public RefreshTokenProvider(@Value("${security.token.refresh.expire-time}") final long expireTime) {
        this.expireTime = expireTime;
    }

    public RefreshToken createToken(final String providerId) {
        final String value = UUID.randomUUID().toString();
        return RefreshToken.from(providerId, value, expireTime);
    }
}
