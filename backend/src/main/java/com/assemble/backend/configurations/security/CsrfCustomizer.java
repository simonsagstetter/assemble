package com.assemble.backend.configurations.security;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.stereotype.Component;

@Component
public class CsrfCustomizer implements Customizer<CsrfConfigurer<HttpSecurity>> {

    @Override
    public void customize( CsrfConfigurer<HttpSecurity> httpSecurityCsrfConfigurer ) {
        httpSecurityCsrfConfigurer
                .csrfTokenRepository(
                        CookieCsrfTokenRepository.withHttpOnlyFalse()
                )
                .csrfTokenRequestHandler(
                        new CsrfTokenRequestAttributeHandler()
                )
                .ignoringRequestMatchers( "/**" );
    }
}
