package org.kakaoshare.backend.domain.funding.exception;

import lombok.Getter;

@Getter
public enum FundingDetailErrorCode {
    NOT_FOUND("기여한 펀딩 내역을 찾을 수 없습니다.");
    private final String message;

    FundingDetailErrorCode(final String message) {
        this.message = message;
    }
}
