package org.kakaoshare.backend.domain.wish.error.exception;

import org.kakaoshare.backend.common.error.ErrorCode;
import org.kakaoshare.backend.common.error.exception.BusinessException;

public class WishException extends BusinessException {
    
    public WishException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
