/*
 * assemble
 * ExceptionHandlingCustomizer.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.configurations.security;

import lombok.AllArgsConstructor;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ExceptionHandlingCustomizer implements Customizer<ExceptionHandlingConfigurer<HttpSecurity>> {

    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Override
    public void customize( ExceptionHandlingConfigurer<HttpSecurity> httpSecurityExceptionHandlingConfigurer ) {
        httpSecurityExceptionHandlingConfigurer
                .authenticationEntryPoint( authenticationEntryPoint )
                .accessDeniedHandler( accessDeniedHandler );
    }
}
