package org.kakaoshare.backend.domain.member.dto.oauth.issue;

public record OAuthReissueRequest(String provider, String refreshToken) {
}
