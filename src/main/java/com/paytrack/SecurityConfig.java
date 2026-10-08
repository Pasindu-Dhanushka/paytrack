package com.paytrack;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .csrf(csrf -> csrf.ignoringRequestMatchers(
                "/api/expenses", "/api/expenses/**"
            ))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/expenses", "/api/expenses/**"
                ).permitAll()
                .anyRequest().authenticated()
            );

        return http.build();
    }
}