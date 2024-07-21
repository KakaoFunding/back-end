package org.kakaoshare.backend.jwt.util;

import jakarta.annotation.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.security.Principal;

public class LoggedInMemberResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoggedInMember.class);
    }

    @Override
    public String resolveArgument(final MethodParameter parameter,
                                  final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest,
                                  final WebDataBinderFactory binderFactory) throws Exception {
        final Principal principal = webRequest.getUserPrincipal();
        try {
            return principal.getName();
        } catch (NullPointerException e) {
            if (parameter.hasParameterAnnotation(Nullable.class)) {
                return null;
            }

            throw new IllegalArgumentException();
        }
    }
}
