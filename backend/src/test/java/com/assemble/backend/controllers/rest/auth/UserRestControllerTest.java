/*
 * assemble
 * UserRestControllerTest.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.controllers.rest.auth;

import com.assemble.backend.models.dtos.auth.ChangePasswordDTO;
import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.models.entities.auth.UserRole;
import com.assemble.backend.repositories.auth.UserRepository;
import com.assemble.backend.services.core.IdService;
import com.assemble.backend.testcontainers.TestcontainersConfiguration;
import com.assemble.backend.testutils.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("UserRestController Integration Test")
@Import(TestcontainersConfiguration.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IdService idService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static User testUser;

    @BeforeEach
    void init() {
        testUser = User.builder()
                .id( idService.generateIdFor( User.class ) )
                .firstname( "Test" )
                .lastname( "User" )
                .username( "testuser" )
                .email( "testuser@example.com" )
                .password( passwordEncoder.encode( "secret" ) )
                .roles( List.of( UserRole.USER ) )
                .build();
    }

    @DisplayName("GET /api/users/me should return 200 and an authenticated user")
    @WithMockCustomUser
    @Test
    void me_ShouldReturnAuthenticatedUser() throws Exception {
        User user = userRepository.save( testUser );

        mockMvc.perform(
                get( "/api/users/me" )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content()
                        .contentType( MediaType.APPLICATION_JSON_VALUE )
        ).andExpect(
                jsonPath( "$.id" ).value( user.getId() )
        ).andExpect(
                jsonPath( "$.username" ).value( user.getUsername() )
        ).andExpect(
                jsonPath( "$.email" ).value( user.getEmail() )
        ).andExpect(
                jsonPath( "$.firstname" ).value( user.getFirstname() )
        ).andExpect(
                jsonPath( "$.lastname" ).value( user.getLastname() )
        );
    }

    @DisplayName("GET /api/users/me should return 404 when authenticated user not found")
    @WithMockCustomUser(username = "fake-user")
    @Test
    void me_ShouldReturn404_WhenUserNotFound() throws Exception {
        userRepository.save( testUser );

        mockMvc.perform(
                get( "/api/users/me" )
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                content()
                        .contentType( MediaType.APPLICATION_JSON_VALUE )
        ).andExpect(
                jsonPath( "$.message" ).value( "User not found" )
        );
    }

    @DisplayName("POST /api/users/change-password should return 404 when authenticated user not found")
    @WithMockCustomUser(username = "fake-user")
    @Test
    void changePassword_ShouldReturn404_WhenUserNotFound() throws Exception {
        userRepository.save( testUser );

        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO(
                "wrong-password",
                "Saf3Password!"
        );

        String jsonContent = objectMapper.writeValueAsString( changePasswordDTO );

        mockMvc.perform(
                post( "/api/users/change-password" )
                        .contentType( MediaType.APPLICATION_JSON_VALUE )
                        .accept( MediaType.APPLICATION_JSON_VALUE )
                        .content( jsonContent )
                        .with( csrf() )
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON_VALUE )
        ).andExpect(
                jsonPath( "$.message" ).value( "User not found" )
        );
    }

    @DisplayName("POST /api/users/change-password should return 400 when old password does not match")
    @WithMockCustomUser
    @Test
    void changePassword_ShouldReturn400_WhenOldPasswordDoesNotMatch() throws Exception {
        userRepository.save( testUser );

        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO(
                "wrong-password",
                "Saf3Password!"
        );

        String jsonContent = objectMapper.writeValueAsString( changePasswordDTO );

        mockMvc.perform(
                post( "/api/users/change-password" )
                        .contentType( MediaType.APPLICATION_JSON_VALUE )
                        .accept( MediaType.APPLICATION_JSON_VALUE )
                        .content( jsonContent )
                        .with( csrf() )
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON_VALUE )
        ).andExpect(
                jsonPath( "$.message" ).value( "Old password does not match" )
        );
    }

    @DisplayName("POST /api/users/change-password should return 400 when new password is invalid")
    @WithMockCustomUser
    @Test
    void changePassword_ShouldReturn400_WhenNewPasswordNotValid() throws Exception {
        userRepository.save( testUser );

        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO(
                "secret",
                "secret"
        );

        String jsonContent = objectMapper.writeValueAsString( changePasswordDTO );

        mockMvc.perform(
                post( "/api/users/change-password" )
                        .contentType( MediaType.APPLICATION_JSON_VALUE )
                        .accept( MediaType.APPLICATION_JSON_VALUE )
                        .content( jsonContent )
                        .with( csrf() )
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON_VALUE )
        ).andExpect(
                jsonPath( "$.errors" ).isArray()
        );
    }

    @DisplayName("POST /api/users/change-password should return 204 when password is changed successfully")
    @WithMockCustomUser
    @Test
    void changePassword_ShouldReturn204() throws Exception {
        userRepository.save( testUser );

        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO(
                "secret",
                "Saf3Password!"
        );

        String jsonContent = objectMapper.writeValueAsString( changePasswordDTO );

        mockMvc.perform(
                post( "/api/users/change-password" )
                        .contentType( MediaType.APPLICATION_JSON_VALUE )
                        .accept( MediaType.APPLICATION_JSON_VALUE )
                        .content( jsonContent )
                        .with( csrf() )
        ).andExpect(
                status().isNoContent()
        );
    }

}