package org.kakaoshare.backend.domain.gift.exception;

public enum GiftErrorCode {
    NOT_FOUND_GIFT("존재하지 않는 선물 내역입니다.");
    private final String message;

    GiftErrorCode(String message) {
        this.message = message;
    }
}
