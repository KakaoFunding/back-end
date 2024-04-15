package org.kakaoshare.backend.domain.member.dto.oauth.authenticate;

import org.kakaoshare.backend.domain.member.dto.oauth.profile.OAuthProfile;

public record OAuthLoginMemberResponse(String profileUrl, String name) {
    public static OAuthLoginMemberResponse from(final OAuthProfile oAuthProfile) {
        return new OAuthLoginMemberResponse(null, oAuthProfile.getName());
    }
}
