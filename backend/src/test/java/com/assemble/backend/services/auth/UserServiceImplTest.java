/*
 * assemble
 * UserServiceImplTest.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.auth;

import com.assemble.backend.exceptions.auth.PasswordMismatchException;
import com.assemble.backend.models.dtos.auth.UserDTO;
import com.assemble.backend.models.entities.auth.SecurityUser;
import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.models.entities.auth.UserRole;
import com.assemble.backend.models.mappers.auth.UserMapper;
import com.assemble.backend.repositories.auth.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName("UserServiceImpl Unit Test")
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();
    private static User user;

    @BeforeAll
    static void init() {
        user = User.builder()
                .id( "1" )
                .username( "testuser" )
                .firstname( "Test" )
                .lastname( "User" )
                .email( "test@example.com" )
                .roles( List.of( UserRole.USER ) )
                .password( encoder.encode( "secret" ) )
                .build();
    }

    @DisplayName("changePassword() should throw when username from principal not found in database")
    @Test
    void changePassword_ShouldThrow_WhenUsernameNotFound() {
        when( userRepository.findByUsername( "not-existing-username" ) )
                .thenReturn( java.util.Optional.empty() );

        EntityNotFoundException e = assertThrows( EntityNotFoundException.class,
                () -> userService.changePassword(
                        "not-existing-username",
                        "old-password",
                        ""
                )
        );

        assertEquals( EntityNotFoundException.class, e.getClass() );
    }

    @DisplayName("changePassword() should throw when password does not match")
    @Test
    void changePassword_ShouldThrow_WhenOldPasswordDoesNotMatch() {
        when( userRepository.findByUsername( "testuser" ) )
                .thenReturn( Optional.of( user ) );

        when( passwordEncoder.matches( "wrong-password", user.getPassword() ) )
                .thenReturn( false );

        PasswordMismatchException e = assertThrows( PasswordMismatchException.class,
                () -> userService.changePassword(
                        "testuser",
                        "wrong-password",
                        ""
                )
        );

        assertEquals( PasswordMismatchException.class, e.getClass() );
    }

    @DisplayName("changePassword() should return true when username found, password matches, a new password is valid")
    @Test
    void changePassword_ShouldReturnTrue_WhenAllValid() {
        when( userRepository.findByUsername( "testuser" ) )
                .thenReturn( Optional.of( user ) );

        when( passwordEncoder.matches( "secret", user.getPassword() ) )
                .thenReturn( true );

        when( passwordEncoder.encode( "new-password" ) )
                .thenReturn( encoder.encode( "new-password" ) );

        assertDoesNotThrow( () -> userService.changePassword(
                "testuser",
                "secret",
                "new-password"
        ) );
    }

    @DisplayName("findMe() should throw when user not found")
    @Test
    void findMe_ShouldThrow_WhenUserNotFound() {
        when( userRepository.findByUsername( Mockito.anyString() ) )
                .thenReturn( Optional.empty() );

        SecurityUser securityUser = new SecurityUser( user );

        EntityNotFoundException e = assertThrows( EntityNotFoundException.class,
                () -> userService.findMe( securityUser )
        );

        assertEquals( EntityNotFoundException.class, e.getClass() );
    }

    @DisplayName("findMe() should return user dto when user was found")
    @Test
    void findMe_ShouldReturnUser_WhenUserFound() {
        when( userRepository.findByUsername( user.getUsername() ) )
                .thenReturn( Optional.of( user ) );

        when( userMapper.toUserDTO( user ) ).thenReturn(
                UserDTO.builder()
                        .id( user.getId() )
                        .firstname( user.getFirstname() )
                        .lastname( user.getLastname() )
                        .email( user.getEmail() )
                        .username( user.getUsername() )
                        .fullname( user.getFirstname() )
                        .build()
        );

        UserDTO userDTO = assertDoesNotThrow( () ->
                userService.findMe( new SecurityUser( user ) )
        );

        assertEquals( user.getUsername(), userDTO.getUsername() );
    }

}