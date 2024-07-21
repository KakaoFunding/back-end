package org.kakaoshare.backend.domain.member.dto.oauth.token;

/**
 * @author kim-minwoo
 * @param refresh_token 요청 시 사용된 리프레시 토큰의 만료 시간이 1개월 미만으로 남았을 때만 갱신되어 전달. 아닌 경우 null
 * @param refresh_token_expires_in 요청 시 사용된 리프레시 토큰의 만료 시간이 1개월 미만으로 남았을 때만 갱신되어 전달. 아닌 경우 null
 */
public record OAuthTokenResponse(String token_type, String access_token, String refresh_token, Long expires_in, Long refresh_token_expires_in) {
}
