package org.kakaoshare.backend.domain.member.dto.oauth.profile;

import java.util.Map;

public abstract class OAuthProfile {
    protected final Map<String, Object> attributes;

    protected OAuthProfile(final Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getGender();
    public abstract String getName();
    public abstract String getPhoneNumber();
    public abstract String getProvider();
    public abstract String getProviderId();
}
