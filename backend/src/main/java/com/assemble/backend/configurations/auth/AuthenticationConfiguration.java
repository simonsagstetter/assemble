package com.assemble.backend.configurations.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthenticationConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager( HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService ) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject( AuthenticationManagerBuilder.class );
        authBuilder
                .userDetailsService( userDetailsService )
                .passwordEncoder( passwordEncoder );

        return authBuilder.build();
    }
}
