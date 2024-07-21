package org.kakaoshare.backend.domain.payment.exception;

import org.kakaoshare.backend.common.error.ErrorCode;
import org.kakaoshare.backend.common.error.exception.BusinessException;

public class PaymentException extends BusinessException {
    public PaymentException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
