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
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.error.MissingEnvironmentVariableException;

import java.util.List;

@Component
@ConditionalOnProperty(name = "assemble.bootstrap", havingValue = "true")
@AllArgsConstructor
public class UserBootstrap implements CommandLineRunner {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private Environment environment;

    @Override
    public void run( String... args ) throws Exception {
        if ( userRepository.count() == 0 ) {
            String username = environment.getProperty( "assemble.superuser.username" );
            String rawPassword = environment.getProperty( "assemble.superuser.password" );

            if ( username == null || rawPassword == null ) {
                throw new MissingEnvironmentVariableException(
                        "With bootstrap set to true you must also configure assemble.superuser.username and assemble.superuser.password."
                );
            }

            User superUser = User.builder()
                    .firstname( "Super" )
                    .lastname( "Assembler" )
                    .username( username )
                    .email( "admin@assemble.com" )
                    .password( passwordEncoder.encode( rawPassword ) )
                    .roles( List.of( UserRole.SUPERUSER ) )
                    .build();

            userRepository.save( superUser );
        }
    }
}
