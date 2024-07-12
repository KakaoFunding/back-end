package org.kakaoshare.backend.domain.member.dto.oauth.issue;

import org.kakaoshare.backend.domain.member.dto.oauth.token.RefreshTokenDto;
import org.kakaoshare.backend.domain.member.entity.token.RefreshToken;

public record ReissueResult(String accessToken, RefreshTokenDto refreshToken) {
    public static ReissueResult of(final String accessToken, final RefreshToken refreshToken) {
        return new ReissueResult(accessToken, RefreshTokenDto.from(refreshToken));
    }
}