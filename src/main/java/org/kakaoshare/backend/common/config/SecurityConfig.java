package org.kakaoshare.backend.common.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String ORIGIN_PATTERN = "*";
    private static final String CORS_CONFIGURATION_PATTERN = "/**";
    public static final String API_V_1 = "/api/v1/";
    
    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .requestMatchers(API_V_1 + "categories/**").permitAll()
                .requestMatchers(API_V_1+"products/**").permitAll()
                .requestMatchers(PathRequest.toH2Console()).permitAll()//TODO 2024 03 02 19:39:16 : 개발단계 이후 제거 요망
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().disable()
                .and()
                .cors();

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern(ORIGIN_PATTERN);
        configuration.addAllowedHeader(ORIGIN_PATTERN);
        configuration.addAllowedMethod(ORIGIN_PATTERN);
        configuration.setAllowCredentials(true);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(CORS_CONFIGURATION_PATTERN, configuration);

        return source;
    }
}
