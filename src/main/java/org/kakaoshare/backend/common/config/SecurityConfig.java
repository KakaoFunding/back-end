package org.kakaoshare.backend.common.config;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.error.handler.AuthenticationAccessDeniedHandler;
import org.kakaoshare.backend.common.error.handler.CustomAuthenticationEntryPoint;
import org.kakaoshare.backend.common.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
//    private static final List<String> ORIGIN_PATTERN = List.of("https://www.kakaofunding.kro.kr/");
    private static final List<String> ORIGIN_PATTERN = List.of("*");
    private static final String CORS_CONFIGURATION_PATTERN = "/**";
    public static final String API_V_1 = "/api/v1/";
    private static final String ACTUATOR = "/actuator/**";
    private static final String FAVICON_URL = "/favicon.ico";

    private static final List<String> ALLOWED_HEADERS = Arrays.asList("Origin", "Content-Type", "Accept", "Authorization", "X-Requested-With");
    private static final List<String> ALLOWED_METHODS = Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS");
    private static final String METRICS = "/metrics";
    
    private final AuthenticationAccessDeniedHandler authenticationAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
                        authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                                .requestMatchers(ACTUATOR).permitAll()
                                .requestMatchers(METRICS).permitAll()
                                .requestMatchers(FAVICON_URL).permitAll()
                                .requestMatchers(API_V_1 + "oauth/login").permitAll()
                                .requestMatchers(API_V_1 + "oauth/logout").authenticated()
                                .requestMatchers(API_V_1 + "oauth/reissue").permitAll()
                                .requestMatchers(API_V_1 + "categories/**").permitAll()
                                .requestMatchers(API_V_1 + "products/**").permitAll()
                                .requestMatchers(API_V_1 + "products/*/wishes").authenticated()
                                .requestMatchers(API_V_1 + "brands/**").permitAll()
                                .requestMatchers(API_V_1 + "search/**").permitAll()
                                .requestMatchers(API_V_1 + "wishes/**").authenticated()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable).disable())
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> corsConfigurationSource())
                .exceptionHandling(
                        httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                                .authenticationEntryPoint(customAuthenticationEntryPoint)
                                .accessDeniedHandler(authenticationAccessDeniedHandler)
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(ORIGIN_PATTERN);
        configuration.setAllowedHeaders(ALLOWED_HEADERS);
        configuration.setAllowedMethods(ALLOWED_METHODS);
        configuration.setAllowCredentials(true);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(CORS_CONFIGURATION_PATTERN, configuration);

        return source;
    }
}
