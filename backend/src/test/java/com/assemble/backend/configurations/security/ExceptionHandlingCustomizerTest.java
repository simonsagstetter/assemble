/*
 * assemble
 * ExceptionHandlingCustomizerTest.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.configurations.security;

import com.assemble.backend.testcontainers.TestcontainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("ExceptionHandlingCustomizer Integration Test")
@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class ExceptionHandlingCustomizerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("AuthenticationEntryPoint should return 401 when unauthorized user tries to access any resource")
    void authenticationEntryPoint_ShouldReturnErrorResponse_WhenUnauthenticatedUser() throws Exception {
        mockMvc.perform(
                post(
                        "/"
                )
        ).andExpect(
                status().isUnauthorized()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.message" ).value( "Not authenticated" )
        );
    }

    @DisplayName("AccessDeniedHandler should return 403 when authenticated user tries to log in again")
    @Test
    @WithMockUser(username = "anyusername", roles = { "USER" })
    void accessDeniedHandler_ShouldReturnErrorResponseFromLoginEndpoint_WhenUserIsAuthenticated() throws Exception {

        mockMvc.perform(
                post(
                        "/api/auth/login"
                )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( """
                                {
                                "username": "user",
                                "password": "password"
                                }
                                """ )
        ).andExpect(
                status().isForbidden()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.message" ).value( "Already authenticated" )
        );
    }
}