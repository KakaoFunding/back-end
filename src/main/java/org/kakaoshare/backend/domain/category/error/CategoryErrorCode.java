package org.kakaoshare.backend.domain.category.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CategoryErrorCode implements ErrorCode {
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND,"cannot find category of requested id"),
    INVALID_SUB_CATEGORY_ID(HttpStatus.NOT_FOUND,"Miss match between category id and subcategory id");
    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getCode() {
        return "";
    }
}
