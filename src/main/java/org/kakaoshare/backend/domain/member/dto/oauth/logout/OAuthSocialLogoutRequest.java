package org.kakaoshare.backend.domain.member.dto.oauth.logout;

public record OAuthSocialLogoutRequest(String provider, String providerId, String socialAccessToken) {
}
