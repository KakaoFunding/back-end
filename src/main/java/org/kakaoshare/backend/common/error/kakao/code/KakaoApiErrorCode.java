package org.kakaoshare.backend.common.error.kakao.code;

import lombok.Getter;
import org.kakaoshare.backend.common.error.ErrorCode;
import org.kakaoshare.backend.common.error.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Objects;

import static org.kakaoshare.backend.common.error.GlobalErrorCode.EXTERNAL_API_ERROR;

@Getter
public enum KakaoApiErrorCode implements ErrorCode {
    INVALID_ACCESS_TOKEN(-401, CODE_PREFIX + "008", HttpStatus.BAD_REQUEST, "유효하지 않은 소셜 엑세스 토큰입니다.");

    private final int serverErrorCode;
    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    KakaoApiErrorCode(final int serverErrorCode, final String code, final HttpStatus httpStatus, final String message) {
        this.serverErrorCode = serverErrorCode;
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public static KakaoApiErrorCode findByServerErrorCode(final int serverErrorCode) {
        return Arrays.stream(values())
                .filter(value -> Objects.equals(value.serverErrorCode, serverErrorCode))
                .findAny()
                .orElseThrow(() -> new BusinessException(EXTERNAL_API_ERROR));
    }
}
