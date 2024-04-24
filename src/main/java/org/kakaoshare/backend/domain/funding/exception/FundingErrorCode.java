package org.kakaoshare.backend.domain.funding.exception;

import lombok.Getter;

@Getter
public enum FundingErrorCode {
    INVALID_ACCUMULATE_AMOUNT("기여 금액은 잔여 금액보다 클 수 없습니다."),
    INVALID_ATTRIBUTE_AMOUNT("기여 금액이 잘못되었습니다."),
    NOT_FOUND("펀딩 내역을 찾을 수 없습니다.");

    private final String message;

    FundingErrorCode(final String message) {
        this.message = message;
    }
}
