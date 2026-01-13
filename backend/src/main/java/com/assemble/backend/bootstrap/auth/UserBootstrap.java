/*
 * assemble
 * UserBootstrap.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.bootstrap.auth;

import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.models.entities.auth.UserRole;
import com.assemble.backend.repositories.auth.UserRepository;
import com.github.f4b6a3.uuid.UuidCreator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Profile("prod")
@AllArgsConstructor
public class UserBootstrap implements CommandLineRunner {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public void run( String... args ) throws Exception {
        if ( userRepository.count() == 0 ) {
            String rawPassword = UuidCreator
                    .getTimeOrderedEpoch()
                    .toString()
                    .replace( "-", "" );

            User superUser = User.builder()
                    .firstname( "Super" )
                    .lastname( "Assemble" )
                    .username( "assemble" )
                    .email( "admin@assemble.com" )
                    .password( passwordEncoder.encode( rawPassword ) )
                    .roles( List.of( UserRole.SUPERUSER ) )
                    .build();

            userRepository.save( superUser );

            log.info( "Superuser created with username: {} and password: {}", superUser.getUsername(), rawPassword );
        }
    }
}
