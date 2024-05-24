package org.kakaoshare.backend.domain.member.dto.oauth.profile.detail;

import org.kakaoshare.backend.domain.member.dto.oauth.profile.OAuthProfile;

import java.util.Map;

public class KakaoProfile extends OAuthProfile {
    public KakaoProfile(final Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getGender() {
        return String.valueOf(getAccount().get("gender"));
    }

    @Override
    public String getName() {
        return String.valueOf(getAccount().get("name"));
    }

    @Override
    public String getPhoneNumber() {
        return String.valueOf(getAccount().get("phone_number"));
    }

    @Override
    public String getProfileImageUrl() {
        return String.valueOf(getProfile().get("profile_image_url"));
    }

    @Override
    public String getProvider() {
        return "KAKAO";
    }

    @Override
    public String getBirthDate() {
        final String birthYear = String.valueOf(getAccount().get("birthyear"));
        final String birthday = String.valueOf(getAccount().get("birthday"));
        return birthYear + "-" + birthday.substring(0, 2) + "-" + birthday.substring(2);
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    private Map<String, Object> getAccount() {
        return (Map<String, Object>) attributes.get("kakao_account");
    }

    private Map<String, Object> getProfile() {
        return (Map<String, Object>) getAccount().get("profile");
    }
}
