package org.kakaoshare.backend.domain.funding.exception;

import lombok.Getter;
import org.kakaoshare.backend.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public enum FundingDetailErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "기여한 펀딩 내역을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    FundingDetailErrorCode(final HttpStatus httpStatus, final String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return "";
    }
}
