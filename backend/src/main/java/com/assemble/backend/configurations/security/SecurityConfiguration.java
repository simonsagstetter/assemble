/*
 * assemble
 * SecurityConfiguration.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.configurations.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@AllArgsConstructor
@Configuration
@EnableMethodSecurity(securedEnabled = true)
@EnableWebSecurity
public class SecurityConfiguration {

    private final CorsCustomizer corsCustomizer;
    private final SessionCustomizer sessionCustomizer;
    private final CsrfCustomizer csrfCustomizer;
    private final SecurityContextCustomizer securityContextCustomizer;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final ExceptionHandlingCustomizer exceptionHandlingCustomizer;
    private final LogoutCustomizer logoutCustomizer;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain( HttpSecurity http ) throws Exception {
        return http
                .cors( corsCustomizer )
                .csrf( csrfCustomizer )
                .sessionManagement( sessionCustomizer )
                .securityContext( securityContextCustomizer )
                .authenticationManager( authenticationManager )
                .userDetailsService( userDetailsService )
                .exceptionHandling( exceptionHandlingCustomizer )
                .httpBasic( AbstractHttpConfigurer::disable )
                .formLogin( AbstractHttpConfigurer::disable )
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers( "/api/auth/login" ).anonymous()
                                .requestMatchers( "/api/auth/logout" ).authenticated()
                                .requestMatchers( "/swagger-ui/**" ).permitAll()
                                .requestMatchers( "/v3/api-docs/**" ).permitAll()
                                .requestMatchers( "/graphiql/**" ).permitAll()
                                .requestMatchers( "/graphql/**" ).permitAll()
                                .anyRequest().authenticated()
                )
                .logout( logoutCustomizer )
                .build();
    }
}
