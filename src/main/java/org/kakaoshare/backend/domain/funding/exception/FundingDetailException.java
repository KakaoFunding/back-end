package org.kakaoshare.backend.domain.funding.exception;

import org.kakaoshare.backend.common.error.ErrorCode;
import org.kakaoshare.backend.common.error.exception.BusinessException;

public class FundingDetailException extends BusinessException {
    public FundingDetailException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
