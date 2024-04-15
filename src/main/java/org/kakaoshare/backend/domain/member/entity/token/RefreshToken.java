package org.kakaoshare.backend.domain.member.entity.token;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash("refresh_token")
public class RefreshToken {
    @Id
    @Column(name = "id", nullable = false)
    private String providerId;

    @Column(unique = true)
    @Indexed
    private String value;

    @TimeToLive
    private Long expiration;

    @Builder
    private RefreshToken(final String providerId, final String value, final Long expiration) {
        this.providerId = providerId;
        this.value = value;
        this.expiration = expiration;
    }

    public static RefreshToken from(final String providerId, final String value, final Long expiration) {
        return RefreshToken.builder()
                .providerId(providerId)
                .value(value)
                .expiration(expiration)
                .build();
    }

    @Override
    public String toString() {
        return "RefreshToken{" +
                "providerId='" + providerId + '\'' +
                ", value='" + value + '\'' +
                ", expiration=" + expiration +
                '}';
    }
}
