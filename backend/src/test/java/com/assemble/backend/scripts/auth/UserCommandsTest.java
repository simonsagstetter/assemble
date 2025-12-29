/*
 * assemble
 * UserCommandsTest.java
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
import com.assemble.backend.testcontainers.TestcontainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Commands Integration Test")
@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserCommandsTest {

    @Autowired
    private UserCommands userCommands;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("create-superuser should create new super user")
    void createSuperUser_ShouldCreateSuperUser() {
        assertDoesNotThrow( () -> {
            String result = userCommands.createSuperUser();
            assertNotNull( result );
        } );

        assertEquals( 1, userRepository.countDistinctByRolesContaining( UserRole.SUPERUSER ) );
    }

    @Test
    @DisplayName("create-superuser should not create new super user when already exists")
    void createSuperUser_ShouldNotCreateSuperUser_WhenAlreadyExists() {
        String rawPassword = UUID.randomUUID().toString().replaceAll( "-", "" );
        User superUser = User.builder()
                .username( "superuser" )
                .firstname( "Super" )
                .lastname( "User" )
                .email( "test@example.com" )
                .password( passwordEncoder.encode( rawPassword ) )
                .roles( List.of( UserRole.SUPERUSER ) )
                .build();

        userRepository.save( superUser );

        assertDoesNotThrow( () -> {
            String result = userCommands.createSuperUser();
            assertNotNull( result );
        } );

        assertEquals( 1, userRepository.countDistinctByRolesContaining( UserRole.SUPERUSER ) );
    }
}