/*
 * assemble
 * LogoutCustomizer.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.configurations.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LogoutCustomizer implements Customizer<LogoutConfigurer<HttpSecurity>> {

    @Override
    public void customize( LogoutConfigurer<HttpSecurity> httpSecurityLogoutConfigurer ) {
        httpSecurityLogoutConfigurer
                .logoutUrl( "/api/auth/logout" )
                .logoutSuccessHandler( ( ( request, response, authentication ) -> {
                    response.setStatus( HttpServletResponse.SC_NO_CONTENT );
                } ) );
    }
}
