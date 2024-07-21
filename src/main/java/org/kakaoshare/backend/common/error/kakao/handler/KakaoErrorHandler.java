package org.kakaoshare.backend.common.error.kakao.handler;

import lombok.extern.slf4j.Slf4j;
import org.kakaoshare.backend.common.error.exception.BusinessException;
import org.kakaoshare.backend.common.error.kakao.code.KakaoApiErrorCode;
import org.kakaoshare.backend.common.error.kakao.code.KakaoAuthErrorCode;
import org.kakaoshare.backend.common.error.kakao.exception.KakaoApiException;
import org.kakaoshare.backend.common.error.kakao.exception.KakaoAuthException;
import org.kakaoshare.backend.common.error.kakao.response.KakaoApiErrorResponse;
import org.kakaoshare.backend.common.error.kakao.response.KakaoAuthErrorResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import static org.kakaoshare.backend.common.error.GlobalErrorCode.EXTERNAL_API_ERROR;

@Service
@Slf4j
public class KakaoErrorHandler {
    public Mono<BusinessException> handleAuth4xxError(final ClientResponse clientResponse) {
        return clientResponse.bodyToMono(KakaoAuthErrorResponse.class)
                .map(response -> KakaoAuthErrorCode.findByServerErrorCode(response.error_code()))
                .map(KakaoAuthException::new);
    }

    public Mono<BusinessException> handleApi4xxError(final ClientResponse clientResponse) {
        return clientResponse.bodyToMono(KakaoApiErrorResponse.class)
                .map(response -> KakaoApiErrorCode.findByServerErrorCode(response.code()))
                .map(KakaoApiException::new);
    }

    public Mono<BusinessException> handle5xxError(final ClientResponse clientResponse) {
        return clientResponse.toBodilessEntity()
                .map(ignore -> new BusinessException(EXTERNAL_API_ERROR));
    }
}
