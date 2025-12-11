/*
 * assemble
 * CorsCustomizer.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.configurations.security;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Component
public class CorsCustomizer implements Customizer<CorsConfigurer<HttpSecurity>> {

    @Override
    public void customize( CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer ) {
        httpSecurityCorsConfigurer
                .configurationSource(
                        corsConfigurationSource()
                );
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsCustomizer = new CorsConfiguration();
        corsCustomizer.addAllowedOrigin( "http://localhost:3000" );
        corsCustomizer.setAllowedHeaders( List.of( "Cookie", "Content-Type", "Accept", "Origin", "X-XSRF-TOKEN" ) );
        corsCustomizer.setAllowedMethods( List.of( "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE" ) );
        corsCustomizer.setAllowCredentials( true );
        corsCustomizer.setMaxAge( 3600L );

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration( "/**", corsCustomizer );
        return source;
    }
}
