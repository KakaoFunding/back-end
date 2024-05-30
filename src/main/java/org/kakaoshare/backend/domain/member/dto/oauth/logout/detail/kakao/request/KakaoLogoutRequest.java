package org.kakaoshare.backend.domain.member.dto.oauth.logout.detail.kakao.request;

public record KakaoLogoutRequest(String target_id_type, Long target_id) {
}
