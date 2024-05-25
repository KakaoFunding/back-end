package org.kakaoshare.backend.domain.member.dto.oauth.authenticate;

import org.kakaoshare.backend.domain.member.dto.oauth.profile.OAuthProfile;

public record OAuthLoginMemberResponse(String providerId, String profileUrl, String birthDate, String name) {
    public static OAuthLoginMemberResponse from(final OAuthProfile oAuthProfile) {
        return new OAuthLoginMemberResponse(
                oAuthProfile.getProviderId(),
                oAuthProfile.getProfileImageUrl(),
                oAuthProfile.getBirthDate(),
                oAuthProfile.getName()
        );
    }
}
