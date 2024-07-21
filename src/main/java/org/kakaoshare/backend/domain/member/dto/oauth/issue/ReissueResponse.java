package org.kakaoshare.backend.domain.member.dto.oauth.issue;

public record ReissueResponse(String accessToken) {
    public static ReissueResponse from(final ReissueResult reissueResult) {
        return new ReissueResponse(reissueResult.accessToken());
    }
}
