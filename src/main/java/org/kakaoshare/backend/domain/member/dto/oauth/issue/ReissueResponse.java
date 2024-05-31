package org.kakaoshare.backend.domain.member.dto.oauth.issue;

import org.kakaoshare.backend.domain.member.dto.oauth.token.RefreshTokenDto;
import org.kakaoshare.backend.domain.member.entity.token.RefreshToken;

public record ReissueResponse(String accessToken, RefreshTokenDto refreshToken) {
    public static ReissueResponse of(final String accessToken, final RefreshToken refreshToken) {
        return new ReissueResponse(accessToken, RefreshTokenDto.from(refreshToken));
    }
}