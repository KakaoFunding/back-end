package org.kakaoshare.backend.domain.funding.exception;

import lombok.Getter;
import org.kakaoshare.backend.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum FundingErrorCode implements ErrorCode {
    INVALID_ATTRIBUTE_AMOUNT(HttpStatus.BAD_REQUEST, "기여 금액이 잘못되었습니다."),
    INVALID_GOAL_AMOUNT(HttpStatus.BAD_REQUEST, "목표 금액이 잘못되었습니다."),

    NOT_FOUND(HttpStatus.NOT_FOUND, "펀딩 내역을 찾을 수 없습니다."),
    INVALID_STATUS(HttpStatus.BAD_REQUEST,"올바른 펀딩의 상태 값이 아닙니다."),
    GOAL_AMOUNT_EXCEEDS_PRODUCT_PRICE(HttpStatus.BAD_REQUEST, "목표 금액은 상품 금액을 초과할 수 없습니다."),
    ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "이미 등록된 펀딩 내역이 있습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    FundingErrorCode(final HttpStatus httpStatus, final String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return "";
    }
}
