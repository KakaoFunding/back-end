package org.kakaoshare.backend.domain.funding.exception;

import lombok.Getter;
import org.kakaoshare.backend.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum FundingErrorCode implements ErrorCode {
    INVALID_ACCUMULATE_AMOUNT(HttpStatus.BAD_REQUEST, "기여 금액은 잔여 금액보다 클 수 없습니다."),
    INVALID_ATTRIBUTE_AMOUNT(HttpStatus.BAD_REQUEST, "기여 금액이 잘못되었습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "펀딩 내역을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    FundingErrorCode(final HttpStatus httpStatus, final String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
