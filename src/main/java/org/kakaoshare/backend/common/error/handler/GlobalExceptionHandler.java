package org.kakaoshare.backend.common.error.handler;

import lombok.extern.slf4j.Slf4j;
import org.kakaoshare.backend.common.error.ErrorCode;
import org.kakaoshare.backend.common.error.GlobalErrorCode;
import org.kakaoshare.backend.common.error.exception.BusinessException;
import org.kakaoshare.backend.common.error.response.ErrorResponse;
import org.kakaoshare.backend.common.util.sort.error.exception.UnsupportedSortTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error(e.getMessage());
        return handleExceptionInternal(GlobalErrorCode.UNSUPPORTED_PARAMETER_TYPE);
    }
    
    @ExceptionHandler(UnsupportedSortTypeException.class)
    protected ResponseEntity<?> handleUnsupportedSortTypeException(UnsupportedSortTypeException e) {
        ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }
    
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<?> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }
    
    private ResponseEntity<?> handleExceptionInternal(ErrorCode errorCode) {
        log.error(errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(errorResponseOf(errorCode));
    }
    
    private ErrorResponse errorResponseOf(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.getHttpStatus().value())
                .message(errorCode.getMessage())
                .build();
    }
}