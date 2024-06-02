package org.kakaoshare.backend.domain.member.service.oauth.detail.kakao;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.member.dto.oauth.logout.detail.kakao.request.KakaoLogoutRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoOAuthRequestProvider {
    private static final String TARGET_ID_TYPE = "user_id"; // TODO: 5/26/24 회원번호 종류, user_id로 고정 (https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#logout-request-admin-key-body)

    public KakaoLogoutRequest createKakaoLogoutRequest(final String providerId) {
        return new KakaoLogoutRequest(TARGET_ID_TYPE, Long.parseLong(providerId));
    }
}
