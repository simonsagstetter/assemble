package com.assemble.backend.configurations.security;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.stereotype.Component;

@Component
public class LogoutCustomizer implements Customizer<LogoutConfigurer<HttpSecurity>> {

    @Override
    public void customize( LogoutConfigurer<HttpSecurity> httpSecurityLogoutConfigurer ) {
        httpSecurityLogoutConfigurer.addLogoutHandler(
                new HeaderWriterLogoutHandler(
                        new ClearSiteDataHeaderWriter(
                                ClearSiteDataHeaderWriter.Directive.COOKIES
                        )
                )
        );
    }
}
