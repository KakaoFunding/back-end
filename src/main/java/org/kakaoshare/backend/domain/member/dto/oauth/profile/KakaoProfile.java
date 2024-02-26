package org.kakaoshare.backend.domain.member.dto.oauth.profile;

import java.util.Map;

public class KakaoProfile extends OAuthProfile {
    public KakaoProfile(final Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getGender() {
        return (String) getAccount().get("gender");
    }

    @Override
    public String getName() {
        return (String) getAccount().get("nickname");
    }

    @Override
    public String getPhoneNumber() {
        return (String) getAccount().get("phone_number");
    }

    @Override
    public String getProvider() {
        return "KAKAO";
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    private Map<String, Object> getAccount() {
        return (Map<String, Object>) attributes.get("kakao_account");
    }

    private Map<String, Object> getProfile() {
        return (Map<String, Object>) getAccount().get("profile");
    }
}
