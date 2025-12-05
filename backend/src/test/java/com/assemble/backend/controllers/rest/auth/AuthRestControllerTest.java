package com.assemble.backend.controllers.rest.auth;

import com.assemble.backend.dtos.auth.LoginRequest;
import com.assemble.backend.models.auth.User;
import com.assemble.backend.models.auth.UserRole;
import com.assemble.backend.repositories.auth.UserRepository;
import com.assemble.backend.repositories.auth.UserRoleRepository;
import com.assemble.backend.services.core.IdService;
import com.assemble.backend.testcontainers.TestcontainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DisplayName("AuthRestController Integration Test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class AuthRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private IdService idService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void login_ShouldReturn401_WhenCredentialsAreInvalid() throws Exception {
        LoginRequest loginRequest = new LoginRequest(
                "not-existing-username",
                "fake-password"
        );

        mockMvc.perform(
                        post(
                                "/api/auth/login"
                        )
                                .contentType( MediaType.APPLICATION_JSON_VALUE )
                                .content( objectMapper.writeValueAsString( loginRequest ) )
                )
                .andExpect(
                        status().isUnauthorized()
                )
                .andExpect(
                        content().contentType( MediaType.APPLICATION_JSON_VALUE )
                )
                .andExpect(
                        jsonPath( "$.message" ).value( "Invalid credentials" )
                );
    }

    @Test
    void login_ShouldReturn200_WhenCredentialsAreValid() throws Exception {
        String rawPassword = "SuperS3curePassword123!";
        UserRole userRole = this.userRoleRepository.findByName( "USER" ).orElseThrow();

        User user = User.builder()
                .id( idService.generateIdFor( User.class ) )
                .username( "mustermannmax" )
                .password( passwordEncoder.encode( rawPassword ) )
                .email( "max.mustermann@example.com" )
                .roles( List.of( userRole ) )
                .build();

        User saved = userRepository.save( user );

        LoginRequest loginRequest = new LoginRequest(
                saved.getUsername(),
                rawPassword
        );

        mockMvc.perform(
                        post(
                                "/api/auth/login"
                        )
                                .contentType( MediaType.APPLICATION_JSON_VALUE )
                                .content( objectMapper.writeValueAsString( loginRequest ) )
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        content().contentType( MediaType.APPLICATION_JSON_VALUE )
                )
                .andExpect(
                        jsonPath( "$.message" ).value( "Login Successful" )
                )
                .andExpect(
                        jsonPath( "$.sessionId" ).isNotEmpty()
                );
    }

    @Test
    @WithMockUser(username = "mustermannmax", roles = { "USER" })
    void logout_ShouldReturn204_WhenUserIsAuthenticated() throws Exception {
        mockMvc.perform(
                        post(
                                "/api/auth/logout"
                        )
                )
                .andExpect(
                        status().isNoContent()
                );
    }
}