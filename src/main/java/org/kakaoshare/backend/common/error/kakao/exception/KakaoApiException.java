package org.kakaoshare.backend.common.error.kakao.exception;

import org.kakaoshare.backend.common.error.ErrorCode;
import org.kakaoshare.backend.common.error.exception.BusinessException;

public class KakaoApiException extends BusinessException {
    public KakaoApiException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
