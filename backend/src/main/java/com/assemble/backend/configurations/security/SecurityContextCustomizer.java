/*
 * assemble
 * SecurityContextCustomizer.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.configurations.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.SecurityContextConfigurer;
import org.springframework.security.web.context.SecurityContextRepository;

@AllArgsConstructor
@Configuration
public class SecurityContextCustomizer implements Customizer<SecurityContextConfigurer<HttpSecurity>> {

    private final SecurityContextRepository securityContextRepository;

    @Override
    public void customize( SecurityContextConfigurer<HttpSecurity> httpSecuritySecurityContextConfigurer ) {
        httpSecuritySecurityContextConfigurer.
                securityContextRepository( securityContextRepository );
    }
}
