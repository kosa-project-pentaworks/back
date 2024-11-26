package com.reservation.global.config;

import com.reservation.global.security.filter.JwtAuthenticationFilter;
import com.reservation.global.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        http.userDetailsService(customUserDetailsService);

        http.authorizeHttpRequests(auth ->
                auth.requestMatchers(
                                "/",
                                "/dashboard",
                                "/api/v1/user/register",
                                "/api/v1/auth/login",
                                "/api/v1/auth/callback",
                                "/api/v1/hospital/search",
                                "/api/v1/hospitalreservation",
                                "/api/v1/payment",
                                "/api/v1/payment/redisremove",
                                "/api/v1/hospitalreservation/list",
                                "/api/v1/hospitalreview/save",
                                "/api/v1/hospitalreservation/update",
                                "/api/v1/payment/refund",
                                "/api/v1/hospitalreview",
                                "/api/v1/hospitalreview/update",
                                "/api/v1/hospitalreview/find",
                                "/api/v1/data",
                                "/swagger-ui.html",          // Swagger UI HTML
                                "/swagger-ui/**",            // Swagger 관련 리소스
                                "/v3/api-docs/**",           // OpenAPI 명세
                                "/swagger-resources/**",     // Swagger 리소스
                                "/webjars/**"                // Webjars
                        ).permitAll()
                        .anyRequest().authenticated()
        );

        http.oauth2Login(oauth2 -> oauth2.failureUrl("/login?error=true"));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedOriginPatterns(Collections.singletonList("*"));
            config.setAllowCredentials(true);
            return config;
        };
    }
}
