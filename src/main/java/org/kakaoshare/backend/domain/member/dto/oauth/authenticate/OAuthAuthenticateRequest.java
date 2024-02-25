package org.kakaoshare.backend.domain.member.dto.oauth.authenticate;

public record OAuthAuthenticateRequest(String registrationId, String code) {
}