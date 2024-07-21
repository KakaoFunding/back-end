package org.kakaoshare.backend.domain.order.exception;

import org.kakaoshare.backend.common.error.ErrorCode;
import org.kakaoshare.backend.common.error.exception.BusinessException;

public class OrderException extends BusinessException {
    public OrderException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
