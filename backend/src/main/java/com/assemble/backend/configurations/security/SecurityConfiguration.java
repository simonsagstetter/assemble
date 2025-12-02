package com.assemble.backend.configurations.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private CorsCustomizer corsCustomizer;

    @Autowired
    private SessionCustomizer sessionCustomizer;

    @Autowired
    private CsrfCustomizer csrfCustomizer;

    @Autowired
    private LogoutCustomizer logoutCustomizer;


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
