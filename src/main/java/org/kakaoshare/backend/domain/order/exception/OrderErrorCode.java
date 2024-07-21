package org.kakaoshare.backend.domain.order.exception;

import lombok.Getter;
import org.kakaoshare.backend.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum OrderErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "주문 내역을 찾을 수 없습니다."),
    DATE_NOT_NULL(HttpStatus.BAD_REQUEST, "조회 시작, 종료일은 필수입니다."),
    INVALID_DATE(HttpStatus.BAD_REQUEST, "조회 시작일은 종료일보다 전이여야 합니다."),
    INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "조회기간은 최대 1년까지 설정 가능합니다.");

    private final HttpStatus httpStatus;
    private final String message;

    OrderErrorCode(final HttpStatus httpStatus, final String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return "";
    }
}
