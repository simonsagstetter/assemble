/*
 * assemble
 * UserCommands.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.scripts.auth;

import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.models.entities.auth.UserRole;
import com.assemble.backend.repositories.auth.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;
import java.util.UUID;

@Slf4j
@ShellComponent
@AllArgsConstructor
public class UserCommands {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @ShellMethod(
            key = "create-superuser"
    )
    // Note: This command is for initial admin setup only. Password is returned in plaintext for
    // one-time configuration and should not be used as a pattern in production auth flows.
    public String createSuperUser() {
        if ( userRepository.countDistinctByRolesContaining( UserRole.SUPERUSER ) == 0 ) {

            String rawPassword = UUID.randomUUID().toString().replace( "-", "" );
            User superUser = User.builder()
                    .firstname( "Simon" )
                    .lastname( "Sagstetter" )
                    .username( "sagstettersi" )
                    .email( "test@example.com" )
                    .password( passwordEncoder.encode( rawPassword ) )
                    .roles( List.of( UserRole.USER, UserRole.ADMIN, UserRole.MANAGER, UserRole.SUPERUSER ) )
                    .build();

            userRepository.save( superUser );

            log.info( "Superuser created with username: superuser and password: {}", rawPassword );

            return "Superuser created with username: superuser and password: " + rawPassword;
        }
        return "Superuser already exists!";
    }

}
