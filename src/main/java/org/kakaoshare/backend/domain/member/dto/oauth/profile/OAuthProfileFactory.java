package org.kakaoshare.backend.domain.member.dto.oauth.profile;

import org.kakaoshare.backend.domain.member.dto.oauth.profile.detail.KakaoProfile;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthProfileFactory {
    KAKAO("kakao", KakaoProfile::new);

    private final String registrationId;
    private final Function<Map<String, Object>, OAuthProfile> mapper;

    OAuthProfileFactory(final String provider,
                        final Function<Map<String, Object>, OAuthProfile> mapper) {
        this.registrationId = provider;
        this.mapper = mapper;
    }

    public static OAuthProfile of(final Map<String, Object> attributes,
                                  final String registrationId) {
        return Arrays.stream(values())
                .filter(value -> value.registrationId.equals(registrationId))
                .findAny()
                .map(value -> value.mapper.apply(attributes))
                .orElseThrow(() -> new IllegalArgumentException());
    }
}
