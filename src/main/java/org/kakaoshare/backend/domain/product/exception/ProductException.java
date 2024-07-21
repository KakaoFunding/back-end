package org.kakaoshare.backend.domain.product.exception;

import org.kakaoshare.backend.common.error.ErrorCode;
import org.kakaoshare.backend.common.error.exception.BusinessException;

public class ProductException extends BusinessException {
    public ProductException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
