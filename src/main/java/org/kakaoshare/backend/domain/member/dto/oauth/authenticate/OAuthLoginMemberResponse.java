package org.kakaoshare.backend.domain.member.dto.oauth.authenticate;

import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.member.dto.oauth.profile.OAuthProfile;

@Getter
public class OAuthLoginMemberResponse {
    private String profileUrl;
    private String name;

    private OAuthLoginMemberResponse() {

    }

    @Builder
    private OAuthLoginMemberResponse(final String profileUrl, final String name) {
        this.profileUrl = profileUrl;
        this.name = name;
    }

    public static OAuthLoginMemberResponse from(final OAuthProfile oAuthProfile) {
        return OAuthLoginMemberResponse.builder()
                .name(oAuthProfile.getName())
                .build();
    }
}
