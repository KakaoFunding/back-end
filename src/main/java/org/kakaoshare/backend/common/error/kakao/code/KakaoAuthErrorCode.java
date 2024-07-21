package org.kakaoshare.backend.common.error.kakao.code;

import lombok.Getter;
import org.kakaoshare.backend.common.error.ErrorCode;
import org.kakaoshare.backend.common.error.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Objects;

import static org.kakaoshare.backend.common.error.GlobalErrorCode.EXTERNAL_API_ERROR;

@Getter
public enum KakaoAuthErrorCode implements ErrorCode {
    NOT_FOUND_REFRESH_TOKEN("KOE319", CODE_PREFIX + "009", HttpStatus.NOT_FOUND, "소셜 리프레시 토큰을 찾을 수 없습니다."),
    INVALID_REFRESH_TOKEN("KOE322", CODE_PREFIX + "010", HttpStatus.NOT_FOUND, "이미 만료되었거나 유효하지 않은 소셜 리프레시 토큰입니다.");

    private final String serverErrorCode;
    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    KakaoAuthErrorCode(final String serverErrorCode, final String code, final HttpStatus httpStatus, final String message) {
        this.serverErrorCode = serverErrorCode;
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public static KakaoAuthErrorCode findByServerErrorCode(final String serverErrorCode) {
        return Arrays.stream(values())
                .filter(value -> Objects.equals(value.serverErrorCode, serverErrorCode))
                .findAny()
                .orElseThrow(() -> new BusinessException(EXTERNAL_API_ERROR));
    }
}
