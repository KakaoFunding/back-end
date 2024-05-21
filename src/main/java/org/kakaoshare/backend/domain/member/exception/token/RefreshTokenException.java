package org.kakaoshare.backend.domain.member.exception.token;

import org.kakaoshare.backend.common.error.ErrorCode;
import org.kakaoshare.backend.common.error.exception.BusinessException;

public class RefreshTokenException extends BusinessException {
    public RefreshTokenException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
