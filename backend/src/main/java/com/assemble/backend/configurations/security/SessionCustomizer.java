package com.assemble.backend.configurations.security;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.stereotype.Component;

@Component
public class SessionCustomizer implements Customizer<SessionManagementConfigurer<HttpSecurity>> {
    @Override
    public void customize( SessionManagementConfigurer<HttpSecurity> httpSecuritySessionManagementConfigurer ) {
        httpSecuritySessionManagementConfigurer
                .sessionFixation( SessionManagementConfigurer.SessionFixationConfigurer::newSession )
                .sessionCreationPolicy( SessionCreationPolicy.IF_REQUIRED )
                .maximumSessions( 3 );
    }
}
