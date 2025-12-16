/*
 * assemble
 * SessionCustomizer.java
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
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SessionCustomizer implements Customizer<SessionManagementConfigurer<HttpSecurity>> {

    private final SessionRegistry sessionRegistry;


    @Override
    public void customize( SessionManagementConfigurer<HttpSecurity> httpSecuritySessionManagementConfigurer ) {
        httpSecuritySessionManagementConfigurer
                .sessionFixation( SessionManagementConfigurer.SessionFixationConfigurer::newSession )
                .sessionCreationPolicy( SessionCreationPolicy.IF_REQUIRED )
                .maximumSessions( 3 )
                .sessionRegistry( sessionRegistry );
    }


}
