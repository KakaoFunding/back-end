package org.kakaoshare.backend.domain.wish.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.kakaoshare.backend.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum WishErrorCode implements ErrorCode {
    DUPLICATED_WISH(HttpStatus.INTERNAL_SERVER_ERROR,"Duplicated wish reservation detected");
    private final HttpStatus httpStatus;
    private final String message;
}
