/*
 * assemble
 * SessionRestControllerTest.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.controllers.rest.auth;

import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.models.entities.auth.UserRole;
import com.assemble.backend.repositories.auth.UserRepository;
import com.assemble.backend.testcontainers.TestcontainersConfiguration;
import com.assemble.backend.testutils.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.util.Date;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Session Rest Controller Integration Test")
@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class SessionRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    @SuppressWarnings("rawtypes")
    private FindByIndexNameSessionRepository sessionRepository;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static User testUser;

    @BeforeEach
    void init() {
        testUser = User.builder()
                .firstname( "Test" )
                .lastname( "User" )
                .username( "testuser" )
                .email( "testuser@example.com" )
                .password( passwordEncoder.encode( "secret" ) )
                .roles( List.of( UserRole.USER, UserRole.ADMIN, UserRole.SUPERUSER ) )
                .build();
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.USER, UserRole.ADMIN, UserRole.SUPERUSER })
    @DisplayName("/GET getActiveUserSessionCount should return 200 and a count of 1 when called")
    @SuppressWarnings("unchecked")
    void getActiveUserSessionCount_ShouldReturn200AndCountOf1_WhenCalled() throws Exception {
        userRepository.save( testUser );
        Session session = sessionRepository.createSession();
        session.setAttribute(
                FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME,
                testUser.getUsername()
        );
        session.setMaxInactiveInterval( Duration.ofSeconds( 7200 ) );

        sessionRepository.save( session );

        SessionInformation sessionInformation = new SessionInformation(
                testUser.getUsername(),
                session.getId(),
                new Date()
        );

        sessionRegistry.registerNewSession( session.getId(), sessionInformation );

        mockMvc.perform(
                        get( "/api/admin/sessions/testuser/count" )
                ).andExpect(
                        status().isOk()
                ).andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect(
                        jsonPath( "$.count" ).value( 1 )
                );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.USER, UserRole.ADMIN, UserRole.SUPERUSER })
    @DisplayName("/DELETE invalidateUserSessions should return 204")
    @SuppressWarnings("unchecked")
    void invalidateUserSessions_ShouldReturn204WhenCalled() throws Exception {
        userRepository.save( testUser );
        Session session = sessionRepository.createSession();
        session.setAttribute(
                FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME,
                testUser.getUsername()
        );
        session.setMaxInactiveInterval( Duration.ofSeconds( 7200 ) );

        sessionRepository.save( session );

        SessionInformation sessionInformation = new SessionInformation(
                testUser.getUsername(),
                session.getId(),
                new Date()
        );

        sessionRegistry.registerNewSession( session.getId(), sessionInformation );

        mockMvc.perform(
                delete( "/api/admin/sessions/testuser" )
                        .with( csrf() )
        ).andExpect(
                status().isNoContent()
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.USER, UserRole.ADMIN, UserRole.SUPERUSER })
    @DisplayName("/GET getUserSessionDetails should return 200 and a list of SessionDTO")
    @SuppressWarnings("unchecked")
    void getUserSessionDetails_ShouldReturn200AndAListOfSessionDTO() throws Exception {
        userRepository.save( testUser );
        Session session = sessionRepository.createSession();
        session.setAttribute(
                FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME,
                testUser.getUsername()
        );
        session.setMaxInactiveInterval( Duration.ofSeconds( 7200 ) );

        sessionRepository.save( session );

        SessionInformation sessionInformation = new SessionInformation(
                testUser.getUsername(),
                session.getId(),
                new Date()
        );

        sessionRegistry.registerNewSession( session.getId(), sessionInformation );

        mockMvc.perform(
                        get( "/api/admin/sessions/testuser" )
                ).andExpect(
                        status().isOk()
                ).andExpect(
                        content().contentType( MediaType.APPLICATION_JSON )
                )
                .andExpect(
                        jsonPath( "$[0].id" ).isNotEmpty()
                ).andExpect(
                        jsonPath( "$[0].sessionId" ).isNotEmpty()
                ).andExpect(
                        jsonPath( "$[0].lastAccessedDate" ).isNotEmpty()
                ).andExpect(
                        jsonPath( "$[0].createdDate" ).isNotEmpty()
                ).andExpect(
                        jsonPath( "$[0].principalName" ).value( "testuser" )
                ).andExpect(
                        jsonPath( "$[0].isExpired" ).value( false )
                );
    }

}