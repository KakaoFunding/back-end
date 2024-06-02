package org.kakaoshare.backend.common.error.response;

import lombok.Builder;
import org.kakaoshare.backend.common.error.ErrorCode;

@Builder
public record ErrorResponse(String message, int code) {
    public static ErrorResponse from(final ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.getHttpStatus().value())
                .message(errorCode.getMessage())
                .build();
    }
}