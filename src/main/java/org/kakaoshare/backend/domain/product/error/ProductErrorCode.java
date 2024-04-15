package org.kakaoshare.backend.domain.product.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.kakaoshare.backend.common.error.ErrorCode;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum ProductErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND,"Product Not Found");
    private final HttpStatus httpStatus;
    private final String message;
}
