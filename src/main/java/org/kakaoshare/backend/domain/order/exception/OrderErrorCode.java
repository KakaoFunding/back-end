package org.kakaoshare.backend.domain.order.exception;

import lombok.Getter;

@Getter
public enum OrderErrorCode {
    NOT_FOUND("주문 내역을 찾을 수 없습니다."),
    DATE_NOT_NULL("조회 시작, 종료일은 필수입니다."),
    INVALID_DATE("조회 시작일은 종료일보다 전이여야 합니다."),
    INVALID_DATE_RANGE("조회기간은 최대 1년까지 설정 가능합니다.");

    private final String message;

    OrderErrorCode(final String message) {
        this.message = message;
    }
}
