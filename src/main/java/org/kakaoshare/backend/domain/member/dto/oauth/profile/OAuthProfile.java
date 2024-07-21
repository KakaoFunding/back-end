package org.kakaoshare.backend.domain.member.dto.oauth.profile;

import org.kakaoshare.backend.common.util.EnumValue;
import org.kakaoshare.backend.domain.member.entity.Gender;
import org.kakaoshare.backend.domain.member.entity.Member;

import java.util.Map;

public abstract class OAuthProfile {
    protected final Map<String, Object> attributes;

    protected OAuthProfile(final Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @EnumValue(enumClass = Gender.class, message = "성별이 잘못되었습니다.", ignoreCase = true)
    public abstract String getGender();
    public abstract String getName();
    public abstract String getPhoneNumber();
    public abstract String getProfileImageUrl();
    public abstract String getProvider();
    public abstract String getBirthDate();
    public abstract String getProviderId();

    public Member toEntity() {
        return Member.builder()
                .name(getName())
                .gender(Gender.from(getGender()))
                .phoneNumber(getPhoneNumber())
                .providerId(getProviderId())
                .profileImageUrl(getProfileImageUrl())
                .build();
    }
}
