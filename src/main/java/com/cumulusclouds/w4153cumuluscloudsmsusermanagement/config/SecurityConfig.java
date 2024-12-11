package com.cumulusclouds.w4153cumuluscloudsmsusermanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.security.JwtUtils;
import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.security.JwtAuthenticationFilter;

import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
public class SecurityConfig {

    private final JwtUtils jwtUtils;

    public SecurityConfig(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OncePerRequestFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtils);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll() // Allow public endpoints
                .requestMatchers("/api/bookers/**").hasAuthority("BOOKER")
                .requestMatchers("/api/musicians/**").hasAuthority("MUSICIAN")
                .requestMatchers("/api/accounts/**").authenticated()
                .anyRequest().authenticated() // Protect all other endpoints
            )
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .httpBasic(); // Enable HTTP Basic Authentication

        return http.build();
    }
}
