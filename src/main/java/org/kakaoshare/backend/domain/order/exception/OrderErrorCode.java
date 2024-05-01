package org.kakaoshare.backend.domain.order.exception;

import lombok.Getter;

@Getter
public enum OrderErrorCode {
    NOT_FOUND("주문 내역을 찾을 수 없습니다.");

    private final String message;

    OrderErrorCode(final String message) {
        this.message = message;
    }
}
