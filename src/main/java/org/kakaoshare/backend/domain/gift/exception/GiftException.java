package org.kakaoshare.backend.domain.gift.exception;


public class GiftException extends RuntimeException {
    private final GiftErrorCode errorCode;

    public GiftException(GiftErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
