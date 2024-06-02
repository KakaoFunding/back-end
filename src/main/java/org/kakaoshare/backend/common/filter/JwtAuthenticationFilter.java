package org.kakaoshare.backend.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;
import org.kakaoshare.backend.common.error.response.ErrorResponse;
import org.kakaoshare.backend.jwt.exception.JwtException;
import org.kakaoshare.backend.jwt.util.JwtProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String TOKEN_PREFIX = "Bearer ";

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    private static void printLog(final JwtException e) {
        log.error("\nException Class = {}\nResponse Code = {}\nMessage = {}",
                e.getClass(),
                e.getErrorCode().getHttpStatus().value(),
                e.getMessage());
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        try {
            jwtProvider.validateToken(accessToken);
            SecurityContextHolder.getContext()
                    .setAuthentication(getAuthentication(accessToken));
        } catch (JwtException e) {
            handleJwtException(response, e);
            printLog(e);
        }

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(final HttpServletRequest request) {
        final String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNullOrEmpty(token)) {
            return null;
        }

        return token.substring(TOKEN_PREFIX.length());
    }

    private Authentication getAuthentication(final String accessToken) {
        final String username = jwtProvider.getUsername(accessToken);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
    }

    private void handleJwtException(final HttpServletResponse response, final JwtException e) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(e.getErrorCode().getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(CharEncoding.UTF_8);
        final ErrorResponse errorResponse = ErrorResponse.from(e.getErrorCode());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
