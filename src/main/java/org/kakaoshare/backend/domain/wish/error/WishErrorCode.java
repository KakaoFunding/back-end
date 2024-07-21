package org.kakaoshare.backend.domain.wish.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.kakaoshare.backend.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum WishErrorCode implements ErrorCode {
    DUPLICATED_WISH(HttpStatus.INTERNAL_SERVER_ERROR, "Duplicated wish reservation detected"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "There's no product in wish list like that"),
    SAVING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Saving wish failed"),
    REMOVING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Removing wish failed");
    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getCode() {
        return "";
    }
}
