package com.assemble.backend.configurations.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@AllArgsConstructor
@Configuration
@EnableMethodSecurity(securedEnabled = true)
@EnableWebSecurity
public class SecurityConfiguration {

    private final CorsCustomizer corsCustomizer;
    private final SessionCustomizer sessionCustomizer;
    private final CsrfCustomizer csrfCustomizer;
    private final SecurityContextCustomizer securityContextCustomizer;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain( HttpSecurity http ) throws Exception {
        return http
                .cors( corsCustomizer )
                .csrf( csrfCustomizer )
                .sessionManagement( sessionCustomizer )
                .securityContext( securityContextCustomizer )
                .authenticationManager( authenticationManager )
                .userDetailsService( userDetailsService )
                .httpBasic( AbstractHttpConfigurer::disable )
                .formLogin( AbstractHttpConfigurer::disable )
                .authorizeHttpRequests(
                        auth -> auth.anyRequest().permitAll()
                )
                .logout( AbstractHttpConfigurer::disable )
                .build();
    }
}
