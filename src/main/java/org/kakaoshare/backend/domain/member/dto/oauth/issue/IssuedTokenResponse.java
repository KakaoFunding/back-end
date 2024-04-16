package org.kakaoshare.backend.domain.member.dto.oauth.issue;

public record IssuedTokenResponse(String accessToken) {
    public static IssuedTokenResponse from(final IssuedTokenResult issuedTokenResult) {
        return new IssuedTokenResponse(issuedTokenResult.accessToken());
    }
}
