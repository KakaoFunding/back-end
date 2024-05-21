package org.kakaoshare.backend.domain.friend.exception;

import org.kakaoshare.backend.common.error.ErrorCode;
import org.kakaoshare.backend.common.error.exception.BusinessException;

public class FriendException extends BusinessException {
    public FriendException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
