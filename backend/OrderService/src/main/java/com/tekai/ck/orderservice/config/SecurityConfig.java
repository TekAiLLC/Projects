/*
 * Copyright (c) 2025 Tek-AI LLC
 * All rights reserved.
 *
 * Created on 11-Apr-2025.
 */

package com.tekai.ck.orderservice.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{

    @SuppressWarnings(value = { "deprecated" })
    @Bean
    SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception
    {
        http.cors().and().csrf().disable().authorizeHttpRequests().requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/orders/**").authenticated() // Allow H2 console access
                .anyRequest().permitAll().and().httpBasic().and().headers().frameOptions().sameOrigin();

        return http.build();
    }

    @Bean
    UserDetailsService userDetailsService()
    {
        final UserDetails user = User.builder().username("user").password(passwordEncoder().encode("password"))
                .roles("USER").build();

        final UserDetails admin = User.builder().username("admin").password(passwordEncoder().encode("admin"))
                .roles("USER", "ADMIN").build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    /**
     * To avoid cors error from UI
     *
     * @return
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource()
    {
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
