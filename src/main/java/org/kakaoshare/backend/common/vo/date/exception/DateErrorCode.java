package org.kakaoshare.backend.common.vo.date.exception;

import lombok.Getter;
import org.kakaoshare.backend.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum DateErrorCode implements ErrorCode {
    INVALID_END_DATE(HttpStatus.BAD_REQUEST, "조회 마감 기간이 잘못되었습니다."),
    NOT_FOUND_DATE(HttpStatus.NOT_FOUND, "조회 시작, 마감 기간은 필수입니다."),
    INVALID_RANGE(HttpStatus.BAD_REQUEST, "조회 마감 기간은 시작 기간 이후여야 합니다.");

    private final HttpStatus httpStatus;
    private final String message;

    DateErrorCode(final HttpStatus httpStatus, final String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return "";
    }
}
