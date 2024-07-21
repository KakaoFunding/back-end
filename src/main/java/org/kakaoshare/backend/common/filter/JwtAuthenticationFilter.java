package org.kakaoshare.backend.common.filter;

import com.querydsl.core.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.kakaoshare.backend.jwt.exception.JwtException;
import org.kakaoshare.backend.jwt.util.JwtProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
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
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String TOKEN_PREFIX = "Bearer ";

    private final String jwtExceptionAttributeName;
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(@Value("${spring.jwt.exception-request-attribute-name}") final String jwtExceptionAttributeName,
                                   final JwtProvider jwtProvider,
                                   final UserDetailsService userDetailsService) {
        this.jwtExceptionAttributeName = jwtExceptionAttributeName;
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final String accessToken = getAccessToken(request);
        try {
            jwtProvider.validateToken(accessToken);
            SecurityContextHolder.getContext()
                    .setAuthentication(getAuthentication(accessToken));
        } catch (JwtException e) {
            request.setAttribute(jwtExceptionAttributeName, e);   // TODO 6/2 인증이 필요없는 API 요청을 대비하여 요청 attribute에 현재 예외 저장
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
}
