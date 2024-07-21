package org.kakaoshare.backend.common.error.exception;

import lombok.Getter;
import org.kakaoshare.backend.common.error.ErrorCode;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
