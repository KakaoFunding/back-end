package org.kakaoshare.backend.common.vo.date.exception;

import org.kakaoshare.backend.common.error.ErrorCode;
import org.kakaoshare.backend.common.error.exception.BusinessException;

public class DateException extends BusinessException {
    public DateException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
