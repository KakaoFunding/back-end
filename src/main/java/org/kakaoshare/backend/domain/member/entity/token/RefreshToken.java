package org.kakaoshare.backend.domain.member.entity.token;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash("refresh_token")
public class RefreshToken {
    @Id
    @Column(name = "refresh_token_id", nullable = false)
    private String refreshTokenId;

    @Column(unique = true)
    private String value;

    @TimeToLive
    private Long expiration;

    @Builder
    private RefreshToken(final String refreshTokenId, final String value, final Long expiration) {
        this.refreshTokenId = refreshTokenId;
        this.value = value;
        this.expiration = expiration;
    }

    public static RefreshToken from(final String providerId, final String value, final Long expiration) {
        return RefreshToken.builder()
                .refreshTokenId(providerId)
                .value(value)
                .expiration(expiration)
                .build();
    }
}
