package org.kakaoshare.backend.logging.config;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.logging.interceptor.LoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class LoggingConfig implements WebMvcConfigurer {
    private static final String METRIC_URL_PREFIX = "/actuator";
    private static final String FAVICON_URL = "/favicon.ico";

    private final LoggingInterceptor loggingInterceptor;

    @Bean
    @RequestScope
    public StopWatch stopWatch() {
        return new StopWatch();
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(METRIC_URL_PREFIX, FAVICON_URL);
    }
}
