package org.kakaoshare.backend.common.error.handler;

import lombok.extern.slf4j.Slf4j;
import org.kakaoshare.backend.common.error.ErrorCode;
import org.kakaoshare.backend.common.error.GlobalErrorCode;
import org.kakaoshare.backend.common.error.exception.BusinessException;
import org.kakaoshare.backend.common.error.response.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.kakaoshare.backend.common.error.GlobalErrorCode.INTERNAL_SERVER_ERROR;

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


    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException e,
                                                                   final HttpHeaders headers,
                                                                   final HttpStatusCode status,
                                                                   final WebRequest request) {
        final GlobalErrorCode errorCode = GlobalErrorCode.RESOURCE_NOT_FOUND;
        logException(e, errorCode);
        return handleExceptionInternal(errorCode);
    }
    
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<?> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        logException(e, errorCode);
        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler(WebClientResponseException.class)
    protected ResponseEntity<?> handleWebClientResponseException(final WebClientResponseException exception) {
        logException(exception, INTERNAL_SERVER_ERROR, exception.getResponseBodyAsString());
        return handleExceptionInternal(INTERNAL_SERVER_ERROR);
    }
    
    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.from(errorCode));
    }
    
    private void logException(final Exception e, final ErrorCode errorCode) {
        log.error(LOG_FORMAT,
                e.getClass(),
                errorCode.getHttpStatus().value(),
                errorCode.getMessage());
    }

    /**
     * ErrorCode가 아닌 예외 객체의 특정 값이 필요한 경우 사용하는 로깅 메서드
     * @param e 예외 클래스
     * @param errorCode 에러 코드
     * @param message 예외 객체의 특정 값
     */
    private void logException(final Exception e, final ErrorCode errorCode, final String message) {
        log.error(LOG_FORMAT,
                e.getClass(),
                errorCode.getHttpStatus().value(),
                message);
    }
}