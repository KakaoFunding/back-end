package org.kakaoshare.backend.domain.cart.exception;

import org.kakaoshare.backend.common.error.ErrorCode;
import org.kakaoshare.backend.common.error.exception.BusinessException;

public class CartException extends BusinessException {
    public CartException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
