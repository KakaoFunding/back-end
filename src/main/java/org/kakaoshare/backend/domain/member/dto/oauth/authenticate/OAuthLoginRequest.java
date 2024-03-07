package org.kakaoshare.backend.domain.member.dto.oauth.authenticate;

public record OAuthLoginRequest(String provider, String code) {
}