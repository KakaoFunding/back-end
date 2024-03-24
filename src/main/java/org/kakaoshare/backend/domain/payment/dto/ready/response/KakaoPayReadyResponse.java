package org.kakaoshare.backend.domain.payment.dto.ready.response;

import java.time.LocalDateTime;

public record KakaoPayReadyResponse(String tid,
                                    String next_redirect_app_url,
                                    String next_mobile_url,
                                    String next_redirect_pc_url,
                                    String android_app_scheme,
                                    String ios_app_scheme,
                                    LocalDateTime created_at) {
}
