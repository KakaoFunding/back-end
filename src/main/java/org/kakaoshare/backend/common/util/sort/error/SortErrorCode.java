package org.kakaoshare.backend.common.util.sort.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.error.ErrorCode;
import org.springframework.http.HttpStatus;
@Getter
@RequiredArgsConstructor
public enum SortErrorCode implements ErrorCode {
    UNSUPPORTED_SORT_TYPE(HttpStatus.BAD_REQUEST,"Unsupported type of sort"),
    NO_MORE_PAGE(HttpStatus.NOT_FOUND,"No more page to response");
    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getCode() {
        return "";
    }
}
