/*
 * assemble
 * CsrfCustomizer.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.configurations.security;

import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CsrfCustomizer implements Customizer<CsrfConfigurer<HttpSecurity>> {

    private Environment environment;

    @Override
    public void customize( CsrfConfigurer<HttpSecurity> httpSecurityCsrfConfigurer ) {
        String domain = environment.getProperty( "server.servlet.session.cookie.domain" );
        CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();

        csrfTokenRepository.setCookieCustomizer( cookie ->
                cookie.domain( domain )
        );

        httpSecurityCsrfConfigurer
                .csrfTokenRepository(
                        csrfTokenRepository
                )
                .csrfTokenRequestHandler(
                        new SpaCsrfTokenRequestHandler()
                )
                .ignoringRequestMatchers( "/api/auth/login" );
    }


}
