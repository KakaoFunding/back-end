package org.kakaoshare.backend.domain.category.error.exception;

import org.kakaoshare.backend.common.error.ErrorCode;
import org.kakaoshare.backend.common.error.exception.BusinessException;

public class CategoryNotFoundException extends BusinessException {
    public CategoryNotFoundException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
