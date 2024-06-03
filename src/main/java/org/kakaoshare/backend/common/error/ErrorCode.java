package org.kakaoshare.backend.common.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String CODE_PREFIX = "KF";

    String getCode();
    String name();
    HttpStatus getHttpStatus();
    String getMessage();
}