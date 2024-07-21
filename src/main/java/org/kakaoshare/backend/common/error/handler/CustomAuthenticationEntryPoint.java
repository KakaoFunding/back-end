package org.kakaoshare.backend.common.error.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;
import org.kakaoshare.backend.common.error.ErrorCode;
import org.kakaoshare.backend.common.error.response.ErrorResponse;
import org.kakaoshare.backend.jwt.exception.JwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final String LOG_EXCEPTION_FORMAT = "Response Code = {}, Message = {}";
    private static final String LOG_URI_FORMAT = "Request Uri = {}";

    private final String jwtExceptionAttributeName;
    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(@Value("${spring.jwt.exception-request-attribute-name}") final String jwtExceptionAttributeName,
                                          final ObjectMapper objectMapper) {
        this.jwtExceptionAttributeName = jwtExceptionAttributeName;
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(final HttpServletRequest request,
                         final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException {
        final JwtException jwtException = (JwtException) request.getAttribute(jwtExceptionAttributeName); // TODO 6/2 JwtAuthenticationFilter 에서 인증 오류 발생 시 request Attribute에 담긴 JwtException 객체를 꺼낸다.
        if (jwtException == null) {
            return;
        }
        printLog(jwtException, request);

        final ErrorCode errorCode = jwtException.getErrorCode();
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(CharEncoding.UTF_8);

        final ErrorResponse errorResponse = ErrorResponse.from(errorCode);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    private void printLog(final JwtException jwtException, final HttpServletRequest request) {
        log.error(LOG_EXCEPTION_FORMAT, jwtException.getErrorCode().getHttpStatus().value(), jwtException.getMessage());
        log.error(LOG_URI_FORMAT, request.getRequestURI());
    }
}