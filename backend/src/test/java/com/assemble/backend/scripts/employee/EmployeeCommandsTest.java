/*
 * assemble
 * EmployeeCommandsTest.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.scripts.employee;

import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.models.entities.auth.UserRole;
import com.assemble.backend.repositories.auth.UserRepository;
import com.assemble.backend.testcontainers.TestcontainersConfiguration;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Employee Commands Integration Test")
@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EmployeeCommandsTest {

    @Autowired
    private EmployeeCommands employeeCommands;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Create-Employee should fail if username parameter is empty")
    void createEmployee_ShouldThrowInvalidParameterException_IfUIsEmpty() {
        assertThrows( InvalidParameterException.class, () -> employeeCommands.createEmployee( "" ) );
    }

    @Test
    @DisplayName("Create-Employee should fail if user does not exist")
    void createEmployee_ShouldThrowEntityNotFoundException_IfUserDoesNotExist() {
        assertThrows( EntityNotFoundException.class, () -> employeeCommands.createEmployee( "testuser" ) );
    }

    @Test
    @DisplayName("Create-Employee should fail if user is already linked to employee")
    void createEmployee_ShouldThrowInvalidParameterException_WhenUserIsAlreadyLinkedToEmployee() {
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

        assertDoesNotThrow( () -> employeeCommands.createEmployee( "superuser" ) );
        assertThrows( InvalidParameterException.class, () -> employeeCommands.createEmployee( "superuser" ) );
    }

    @Test
    @DisplayName("Create-Employee should return id if command ran successful")
    void createEmployee_ShouldReturnId_WhenCommandRanSuccessful() {
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

        String employeeId = assertDoesNotThrow( () -> employeeCommands.createEmployee( "superuser" ) );

        assertNotNull( employeeId );
    }

}