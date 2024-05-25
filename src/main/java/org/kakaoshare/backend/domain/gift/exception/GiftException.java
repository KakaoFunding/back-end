package org.kakaoshare.backend.domain.gift.exception;


import org.kakaoshare.backend.common.error.ErrorCode;
import org.kakaoshare.backend.common.error.exception.BusinessException;

public class GiftException extends BusinessException {
    public GiftException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
