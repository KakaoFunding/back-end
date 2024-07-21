package org.kakaoshare.backend.domain.member.exception;

import org.kakaoshare.backend.common.error.ErrorCode;
import org.kakaoshare.backend.common.error.exception.BusinessException;

public class MemberException extends BusinessException {
    public MemberException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
