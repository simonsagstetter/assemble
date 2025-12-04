package com.assemble.backend.configurations.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final CorsCustomizer corsCustomizer;
    private final SessionCustomizer sessionCustomizer;
    private final CsrfCustomizer csrfCustomizer;
    private final LogoutCustomizer logoutCustomizer;

    public SecurityConfiguration(
            CorsCustomizer corsCustomizer,
            SessionCustomizer sessionCustomizer,
            CsrfCustomizer csrfCustomizer,
            LogoutCustomizer logoutCustomizer
    ) {
        this.corsCustomizer = corsCustomizer;
        this.sessionCustomizer = sessionCustomizer;
        this.csrfCustomizer = csrfCustomizer;
        this.logoutCustomizer = logoutCustomizer;

    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain( HttpSecurity http ) throws Exception {
        return http
                .cors( corsCustomizer )
                .csrf( csrfCustomizer )
                .sessionManagement( sessionCustomizer )
                .httpBasic( AbstractHttpConfigurer::disable )
                .formLogin( AbstractHttpConfigurer::disable )
                .authorizeHttpRequests(
                        auth -> auth.anyRequest().permitAll()
                )
                .logout( logoutCustomizer )
                .build();
    }
}
