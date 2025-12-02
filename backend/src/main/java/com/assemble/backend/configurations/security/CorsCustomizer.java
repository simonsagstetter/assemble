package com.assemble.backend.configurations.security;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
        corsCustomizer.setAllowCredentials( true );
        corsCustomizer.addAllowedOrigin( "http://localhost:3000" );
        corsCustomizer.addAllowedHeader( "*" );
        corsCustomizer.addAllowedMethod( "OPTIONS" );
        corsCustomizer.addAllowedMethod( "GET" );
        corsCustomizer.addAllowedMethod( "POST" );
        corsCustomizer.addAllowedMethod( "PUT" );
        corsCustomizer.addAllowedMethod( "PATCH" );
        corsCustomizer.addAllowedMethod( "DELETE" );

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration( "/**", corsCustomizer );
        return source;
    }
}
