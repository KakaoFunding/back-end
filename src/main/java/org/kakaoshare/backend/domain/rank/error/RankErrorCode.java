package org.kakaoshare.backend.domain.rank.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RankErrorCode implements ErrorCode {
    INCORRECT_PRICE_RANGE(HttpStatus.BAD_REQUEST, "Not in the correct price range");
    private final HttpStatus httpStatus;
    private final String message;
}
