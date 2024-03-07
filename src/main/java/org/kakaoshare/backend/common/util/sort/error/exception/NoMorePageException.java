package org.kakaoshare.backend.common.util.sort.error.exception;

import lombok.Getter;
import org.kakaoshare.backend.common.error.ErrorCode;
import org.kakaoshare.backend.common.error.exception.BusinessException;

@Getter
public class NoMorePageException extends BusinessException {
    public NoMorePageException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
