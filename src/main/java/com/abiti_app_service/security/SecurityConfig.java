package com.abiti_app_service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private DeviceSessionFilter deviceSessionFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // Disable CSRF
            .csrf(csrf -> csrf.disable())

            // Allow H2 console in iframe
            .headers(headers -> headers
                    .frameOptions(frame -> frame.disable())
            )

            // Allow APIs
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/h2-console/**").permitAll()  // H2 console allow
                    .anyRequest().permitAll()
            )

            // Custom Device Session Filter
            .addFilterBefore(deviceSessionFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}