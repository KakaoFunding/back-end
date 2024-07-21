package org.kakaoshare.backend.domain.payment.exception;

import lombok.Getter;
import org.kakaoshare.backend.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum PaymentErrorCode implements ErrorCode {
    INVALID_AMOUNT(HttpStatus.BAD_REQUEST, "결제 금액이 올바르지 않습니다."),
    INVALID_OPTION(HttpStatus.BAD_REQUEST, "선택한 옵션이 올바르지 않습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "결제 내역을 찾을 수 없습니다."),
    ALREADY_REFUND(HttpStatus.BAD_REQUEST, "이미 환불된 내역입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    PaymentErrorCode(final HttpStatus httpStatus, final String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return "";
    }
}
