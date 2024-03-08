package org.kakaoshare.backend.common.error.handler;

import lombok.extern.slf4j.Slf4j;
import org.kakaoshare.backend.common.error.ErrorCode;
import org.kakaoshare.backend.common.error.GlobalErrorCode;
import org.kakaoshare.backend.common.error.exception.BusinessException;
import org.kakaoshare.backend.common.error.response.ErrorResponse;
import org.kakaoshare.backend.common.util.sort.error.exception.UnsupportedSortTypeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String LOG_FORMAT = "\nException Class = {}\nResponse Code = {}\nMessage = {}";
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        GlobalErrorCode errorCode = GlobalErrorCode.UNSUPPORTED_PARAMETER_TYPE;
        logException(e, errorCode);
        return handleExceptionInternal(errorCode);
    }
    
    @Nullable
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException e,
                                                                          final HttpHeaders headers,
                                                                          final HttpStatusCode status,
                                                                          final WebRequest request) {
        GlobalErrorCode errorCode = GlobalErrorCode.UNSUPPORTED_PARAMETER_NAME;
        logException(e, errorCode);
        return handleExceptionInternal(errorCode);
    }
    
    @ExceptionHandler(UnsupportedSortTypeException.class)
    protected ResponseEntity<?> handleUnsupportedSortTypeException(UnsupportedSortTypeException e) {
        ErrorCode errorCode = e.getErrorCode();
        logException(e, errorCode);
        return handleExceptionInternal(errorCode);
    }
    
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<?> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        logException(e, errorCode);
        return handleExceptionInternal(errorCode);
    }
    
    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
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
    
    private static void logException(final Exception e, final ErrorCode errorCode) {
        log.error(LOG_FORMAT,
                e.getClass(),
                errorCode.getHttpStatus().value(),
                errorCode.getMessage());
    }
}