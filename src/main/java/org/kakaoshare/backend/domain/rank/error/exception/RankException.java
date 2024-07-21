package org.kakaoshare.backend.domain.rank.error.exception;

import org.kakaoshare.backend.common.error.ErrorCode;
import org.kakaoshare.backend.common.error.exception.BusinessException;

public class RankException extends BusinessException {
    public RankException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
