package org.kakaoshare.backend.domain.member.exception.oauth;

import org.kakaoshare.backend.common.error.ErrorCode;
import org.kakaoshare.backend.common.error.exception.BusinessException;

public class OAuthAccessTokenException extends BusinessException {
    public OAuthAccessTokenException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
