package org.kakaoshare.backend.jwt.exception;

import org.kakaoshare.backend.common.error.ErrorCode;
import org.kakaoshare.backend.common.error.exception.BusinessException;

public class JwtException extends BusinessException {
    public JwtException(final ErrorCode errorCode) {
        super(errorCode);
    }
}