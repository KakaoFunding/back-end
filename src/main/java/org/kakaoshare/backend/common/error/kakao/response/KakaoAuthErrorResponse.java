package org.kakaoshare.backend.common.error.kakao.response;

public record KakaoAuthErrorResponse(String error_code, String error, String error_description) {
}
