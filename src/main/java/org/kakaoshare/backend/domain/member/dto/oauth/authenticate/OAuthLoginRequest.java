package org.kakaoshare.backend.domain.member.dto.oauth.authenticate;

import jakarta.validation.constraints.NotNull;

public record OAuthLoginRequest(
        @NotNull(message = "소셜 이름은 필수입니다.") String provider,
        @NotNull(message = "소셜 리프레시 토큰은 필수입니다.") String socialAccessToken
) {}