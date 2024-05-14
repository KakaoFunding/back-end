package org.kakaoshare.backend.common.config;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
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
    private static final List<String> ORIGIN_PATTERN = List.of("https://kakao-funding-git-refactor-241-teamfunding.vercel.app");
    private static final String CORS_CONFIGURATION_PATTERN = "/**";
    public static final String API_V_1 = "/api/v1/";
    private static final List<String> ALLOWED_HEADERS = Arrays.asList("Origin", "Content-Type", "Accept", "Authorization", "X-Requested-With");
    private static final List<String> ALLOWED_METHODS = Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS");
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .requestMatchers(API_V_1 + "oauth/login").permitAll()
                .requestMatchers(API_V_1 + "oauth/logout").authenticated()
                .requestMatchers(API_V_1 + "categories/**").permitAll()
                .requestMatchers(API_V_1 + "products/**").permitAll()
                .requestMatchers(API_V_1 + "products/*/wishes").authenticated()
                .requestMatchers(API_V_1 + "brands/**").permitAll()
                .requestMatchers(API_V_1 + "search/**").permitAll()
                .requestMatchers(API_V_1 + "wishes/**").authenticated()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .headers().frameOptions().disable()
                .and()
                .cors();
        
        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(ORIGIN_PATTERN);
//        configuration.setAllowedHeaders(ALLOWED_HEADERS);
//        configuration.setAllowedMethods(ALLOWED_METHODS);
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(CORS_CONFIGURATION_PATTERN, configuration);

        return source;
    }
}
