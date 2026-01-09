/*
 * assemble
 * UserAdminRestControllerTest.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.controllers.rest.auth;

import com.assemble.backend.models.dtos.auth.admin.*;
import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.models.entities.auth.UserRole;
import com.assemble.backend.models.entities.employee.Employee;
import com.assemble.backend.repositories.auth.UserRepository;
import com.assemble.backend.repositories.employee.EmployeeRepository;
import com.assemble.backend.testcontainers.TestcontainersConfiguration;
import com.assemble.backend.testutils.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.f4b6a3.uuid.UuidCreator;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DisplayName("UserAdminRestController Integration Test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class UserAdminRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    @SuppressWarnings("rawtypes")
    private FindByIndexNameSessionRepository sessionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static User testUser;

    private static Employee testEmployee;

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


        testEmployee = Employee.builder()
                .firstname( "Max" )
                .lastname( "Mustermann" )
                .email( "testuser@example.com" )
                .build();

    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/GET getAllUsers should return 200")
    void getAllUsers_ShouldReturn200_WhenCalled() throws Exception {
        userRepository.save( testUser );

        mockMvc.perform(
                get( "/api/admin/users" )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$[0].id" ).isNotEmpty()

        ).andExpect(
                jsonPath( "$[0].username" ).value( testUser.getUsername() )
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/GET getUserById should return 200 when user found")
    void getUserById_ShouldReturn200_WhenUserFound() throws Exception {
        userRepository.save( testUser );

        mockMvc.perform(
                get( "/api/admin/users/" + testUser.getId() )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.id" ).isNotEmpty()

        ).andExpect(
                jsonPath( "$.username" ).value( testUser.getUsername() )
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/GET getUserById should return 404 when user not found")
    void getUserById_ShouldReturn404_WhenUserNotFound() throws Exception {
        userRepository.save( testUser );

        mockMvc.perform(
                get( "/api/admin/users/" + UuidCreator.getTimeOrderedEpoch().toString() )
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.message" ).isNotEmpty()

        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/GET searchUnlinkedUsers should return 200")
    void searchUnlinkedUsers_ShouldReturn200_WhenCalled() throws Exception {
        User saved = userRepository.save( testUser );

        assert saved.getId() != null;

        mockMvc.perform(
                get( "/api/admin/users/search/" + testUser.getFirstname().toLowerCase() )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$[0].id" ).value( saved.getId().toString() )

        );
    }


    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/POST createUser should return 400 when request body is invalid")
    void createUser_ShouldReturn400_WhenRequestBodyIsInvalid() throws Exception {
        userRepository.save( testUser );

        UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                .username( "max" )
                .password( "unsafe" )
                .firstname( "" )
                .lastname( "" )
                .email( "invalid@" )
                .roles( List.of() )
                .build();

        String jsonContent = objectMapper.writeValueAsString( userCreateDTO );

        mockMvc.perform(
                post( "/api/admin/users" )
                        .with( csrf() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.errors" ).isNotEmpty()

        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/POST createUser should return 409 when username is not unique")
    void createUser_ShouldReturn409_WhenUserNameIsNotUnique() throws Exception {
        userRepository.save( testUser );

        UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                .username( "testuser" )
                .password( "SuperSaf3Passw0rd!" )
                .firstname( "Max" )
                .lastname( "Musterperson" )
                .email( "max@example.com" )
                .roles( List.of( UserRole.USER ) )
                .build();

        String jsonContent = objectMapper.writeValueAsString( userCreateDTO );

        mockMvc.perform(
                post( "/api/admin/users" )
                        .with( csrf() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
        ).andExpect(
                status().isConflict()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.message" ).isNotEmpty()

        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/POST createUser should return 201 when request body is valid")
    void createUser_ShouldReturn201_WhenRequestBodyIsValid() throws Exception {
        userRepository.save( testUser );

        UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                .username( "musterperson" )
                .firstname( "Max" )
                .lastname( "Musterperson" )
                .email( "max@example.com" )
                .roles( List.of( UserRole.USER ) )
                .build();

        String jsonContent = objectMapper.writeValueAsString( userCreateDTO );

        mockMvc.perform(
                post( "/api/admin/users" )
                        .with( csrf() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
        ).andExpect(
                status().isCreated()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.username" ).value( userCreateDTO.getUsername() )
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/POST createUser should return 201 when request body with password is valid")
    void createUser_ShouldReturn201_WhenRequestBodyWithPasswordIsValid() throws Exception {
        userRepository.save( testUser );

        UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                .username( "musterperson" )
                .password( "SuperSaf3Passw0rd!" )
                .firstname( "Max" )
                .lastname( "Musterperson" )
                .email( "max@example.com" )
                .roles( List.of( UserRole.USER ) )
                .build();

        String jsonContent = objectMapper.writeValueAsString( userCreateDTO );

        mockMvc.perform(
                post( "/api/admin/users" )
                        .with( csrf() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
        ).andExpect(
                status().isCreated()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.username" ).value( userCreateDTO.getUsername() )
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/POST createUser should return 400 when employee is already linked")
    void createUser_ShouldReturn400_WhenEmployeeIsAlreadyLinked() throws Exception {
        User saved = userRepository.save( testUser );
        testEmployee.setUser( saved );
        employeeRepository.save( testEmployee );

        assert testEmployee.getId() != null;

        UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                .username( "musterperson" )
                .password( "SuperSaf3Passw0rd!" )
                .firstname( "Max" )
                .lastname( "Musterperson" )
                .email( "max@example.com" )
                .roles( List.of( UserRole.USER ) )
                .employeeId( testEmployee.getId().toString() )
                .build();

        String jsonContent = objectMapper.writeValueAsString( userCreateDTO );

        mockMvc.perform(
                post( "/api/admin/users" )
                        .with( csrf() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.message" ).isNotEmpty()
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/POST createUser should return 201 when employee is not linked")
    void createUser_ShouldReturn201_WhenEmployeeIsNotLinked() throws Exception {
        userRepository.save( testUser );
        Employee savedEmployee = employeeRepository.save( testEmployee );

        assert savedEmployee.getId() != null;

        UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                .username( "musterperson" )
                .password( "SuperSaf3Passw0rd!" )
                .firstname( "Max" )
                .lastname( "Musterperson" )
                .email( "max@example.com" )
                .roles( List.of( UserRole.USER ) )
                .employeeId( savedEmployee.getId().toString() )
                .build();

        String jsonContent = objectMapper.writeValueAsString( userCreateDTO );

        mockMvc.perform(
                post( "/api/admin/users" )
                        .with( csrf() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
        ).andExpect(
                status().isCreated()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.employee.id" ).value( savedEmployee.getId().toString() )
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/PATCH updateUser should return 400 when dto is invalid")
    void updateUser_ShouldReturn400_WhenDTOIsInvalid() throws Exception {
        userRepository.save( testUser );

        UserUpdateDTO userUpdateDTO = UserUpdateDTO.builder()
                .username( "max" )
                .firstname( "" )
                .lastname( "" )
                .email( "invalid@" )
                .build();

        String jsonContent = objectMapper.writeValueAsString( userUpdateDTO );

        mockMvc.perform(
                patch( "/api/admin/users/" + testUser.getId() )
                        .with( csrf() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.errors" ).isNotEmpty()
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/PATCH updateUser should return 404 when user does not exist")
    void updateUser_ShouldReturn404_WhenUserDoesNotExist() throws Exception {
        userRepository.save( testUser );

        UserUpdateDTO userUpdateDTO = UserUpdateDTO.builder()
                .username( "newusername" )
                .firstname( "Marlon" )
                .lastname( "Musterperson" )
                .email( "valid@email.com" )
                .build();

        String jsonContent = objectMapper.writeValueAsString( userUpdateDTO );

        mockMvc.perform(
                patch( "/api/admin/users/" + UuidCreator.getTimeOrderedEpoch().toString() )
                        .with( csrf() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.message" ).isNotEmpty()
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/PATCH updateUser should return 200 when called")
    void updateUser_ShouldReturn200_WhenCalled() throws Exception {
        userRepository.save( testUser );

        UserUpdateDTO userUpdateDTO = UserUpdateDTO.builder()
                .username( "newusername" )
                .firstname( "Marlon" )
                .lastname( "Musterperson" )
                .email( "valid@email.com" )
                .build();

        String jsonContent = objectMapper.writeValueAsString( userUpdateDTO );

        mockMvc.perform(
                patch( "/api/admin/users/" + testUser.getId() )
                        .with( csrf() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.username" ).value( userUpdateDTO.getUsername() )
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/PATCH setPassword should return 400 when dto is invalid")
    void updateUserPassword_ShouldReturn400_WhenDTOIsInvalid() throws Exception {
        userRepository.save( testUser );

        UserUpdatePasswordDTO userUpdatePasswordDTO = UserUpdatePasswordDTO.builder()
                .newPassword( "123" )
                .invalidateAllSessions( false )
                .build();

        String jsonContent = objectMapper.writeValueAsString( userUpdatePasswordDTO );

        mockMvc.perform(
                patch( "/api/admin/users/" + testUser.getId() + "/password" )
                        .with( csrf() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.errors" ).isNotEmpty()
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/PATCH setPassword should return 404 when user doest not exist")
    void updateUserPassword_ShouldReturn404_WhenUserDoesNotExist() throws Exception {
        userRepository.save( testUser );

        UserUpdatePasswordDTO userUpdatePasswordDTO = UserUpdatePasswordDTO.builder()
                .newPassword( "B3tterPassword!" )
                .invalidateAllSessions( false )
                .build();

        String jsonContent = objectMapper.writeValueAsString( userUpdatePasswordDTO );

        UUID randomId = UuidCreator.getTimeOrderedEpoch();

        mockMvc.perform(
                patch( "/api/admin/users/" + randomId.toString() + "/password" )
                        .with( csrf() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.message" ).isNotEmpty()
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/PATCH setPassword should return 204 when called")
    @SuppressWarnings("unchecked")
    void updateUserPassword_ShouldReturn204_WhenCalled() throws Exception {
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

        UserUpdatePasswordDTO userUpdatePasswordDTO = UserUpdatePasswordDTO.builder()
                .newPassword( "B3tterPassword!" )
                .invalidateAllSessions( false )
                .build();

        String jsonContent = objectMapper.writeValueAsString( userUpdatePasswordDTO );

        mockMvc.perform(
                patch( "/api/admin/users/" + testUser.getId() + "/password" )
                        .with( csrf() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
        ).andExpect(
                status().isNoContent()
        );

        Integer size = sessionRepository.findByPrincipalName( testUser.getUsername() ).size();
        assertThat( size ).isEqualTo( 1 );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/PATCH setPassword should return 204 and delete sessions when called")
    @SuppressWarnings("unchecked")
    void updateUserPassword_ShouldReturn204AndDeleteSessions_WhenCalled() throws Exception {
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

        UserUpdatePasswordDTO userUpdatePasswordDTO = UserUpdatePasswordDTO.builder()
                .newPassword( "B3tterPassword!" )
                .invalidateAllSessions( true )
                .build();

        String jsonContent = objectMapper.writeValueAsString( userUpdatePasswordDTO );

        mockMvc.perform(
                patch( "/api/admin/users/" + testUser.getId() + "/password" )
                        .with( csrf() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
        ).andExpect(
                status().isNoContent()
        );

        Integer size = sessionRepository.findByPrincipalName( testUser.getUsername() ).size();
        assertThat( size ).isZero();
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/DELETE deleteUser should return 404 when user does not exist")
    void deleteUser_ShouldReturn404_WhenUserDoesNotExist() throws Exception {
        userRepository.save( testUser );

        mockMvc.perform(
                delete( "/api/admin/users/" + UuidCreator.getTimeOrderedEpoch().toString() )
                        .with( csrf() )
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.message" ).isNotEmpty()
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/DELETE deleteUser should return 204")
    void deleteUser_ShouldReturn204_WhenCalled() throws Exception {
        userRepository.save( testUser );

        mockMvc.perform(
                delete( "/api/admin/users/" + testUser.getId() )
                        .with( csrf() )
        ).andExpect(
                status().isNoContent()
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/DELETE deleteUser should return 204 and update related employee")
    void deleteUser_ShouldReturn204AndUpdateRelatedEmployee_WhenCalled() throws Exception {
        User saved = userRepository.save( testUser );
        testEmployee.setUser( saved );
        employeeRepository.save( testEmployee );

        mockMvc.perform(
                delete( "/api/admin/users/" + testUser.getId() )
                        .with( csrf() )
        ).andExpect(
                status().isNoContent()
        );

        assert testEmployee.getId() != null;

        Employee updatedEmployee = employeeRepository.findById( testEmployee.getId() ).orElseThrow();
        assertThat( updatedEmployee.getUser() ).isNull();
    }


    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/Patch updateUserStatus should return 404 when user does not exist")
    void updateUserStatus_ShouldReturn404_WhenUserDoesNotExist() throws Exception {
        userRepository.save( testUser );

        UserUpdateStatusDTO userUpdateStatusDTO = UserUpdateStatusDTO.builder()
                .enabled( true )
                .locked( false )
                .build();

        String jsonContent = objectMapper.writeValueAsString( userUpdateStatusDTO );

        mockMvc.perform(
                patch( "/api/admin/users/" + UuidCreator.getTimeOrderedEpoch().toString() + "/status" )
                        .with( csrf() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.message" ).isNotEmpty()
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/Patch updateUserStatus should return 200 when user does exist")
    void updateUserStatus_ShouldReturn200_WhenUserDoesExist() throws Exception {
        User saved = userRepository.save( testUser );

        UserUpdateStatusDTO userUpdateStatusDTO = UserUpdateStatusDTO.builder()
                .enabled( true )
                .locked( false )
                .build();

        String jsonContent = objectMapper.writeValueAsString( userUpdateStatusDTO );

        assert saved.getId() != null;

        mockMvc.perform(
                patch( "/api/admin/users/" + testUser.getId() + "/status" )
                        .with( csrf() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.id" ).value( saved.getId().toString() )
        ).andExpect(
                jsonPath( "$.enabled" ).value( userUpdateStatusDTO.getEnabled() )
        ).andExpect(
                jsonPath( "$.locked" ).value( userUpdateStatusDTO.getLocked() )
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/Patch updateUserStatus should return 200 and invalidate sessions when user does exist")
    @SuppressWarnings("unchecked")
    void updateUserStatus_ShouldReturn200AndInvalidateSessions_WhenUserDoesExist() throws Exception {
        User saved = userRepository.save( testUser );
        assert saved.getId() != null;

        Session session = sessionRepository.createSession();
        session.setAttribute(
                FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME,
                saved.getUsername()
        );
        session.setMaxInactiveInterval( Duration.ofSeconds( 7200 ) );

        sessionRepository.save( session );

        SessionInformation sessionInformation = new SessionInformation(
                saved.getUsername(),
                session.getId(),
                new Date()
        );

        sessionRegistry.registerNewSession( session.getId(), sessionInformation );

        UserUpdateStatusDTO userUpdateStatusDTO = UserUpdateStatusDTO.builder()
                .enabled( false )
                .locked( true )
                .build();

        String jsonContent = objectMapper.writeValueAsString( userUpdateStatusDTO );

        mockMvc.perform(
                patch( "/api/admin/users/" + saved.getId().toString() + "/status" )
                        .with( csrf() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.id" ).value( saved.getId().toString() )
        ).andExpect(
                jsonPath( "$.enabled" ).value( userUpdateStatusDTO.getEnabled() )
        ).andExpect(
                jsonPath( "$.locked" ).value( userUpdateStatusDTO.getLocked() )
        );

        Integer size = sessionRepository.findByPrincipalName( saved.getUsername() ).size();
        assertThat( size ).isZero();
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/Patch updateUserRoles should return 400 when roles value invalid")
    void updateUserRoles_ShouldReturn400_WhenRolesValueInvalid() throws Exception {
        userRepository.save( testUser );

        UserUpdateRolesDTO userUpdateRolesDTO = UserUpdateRolesDTO.builder()
                .roles( List.of() )
                .build();

        String jsonContent = objectMapper.writeValueAsString( userUpdateRolesDTO );

        mockMvc.perform(
                patch( "/api/admin/users/fake-id/roles" )
                        .with( csrf() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.errors" ).isNotEmpty()
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/Patch updateUserRoles should return 404 when user does not exist")
    void updateUserRoles_ShouldReturn404_WhenUserDoesNotExist() throws Exception {
        userRepository.save( testUser );

        UserUpdateRolesDTO userUpdateRolesDTO = UserUpdateRolesDTO.builder()
                .roles( List.of( UserRole.SUPERUSER, UserRole.ADMIN ) )
                .build();

        String jsonContent = objectMapper.writeValueAsString( userUpdateRolesDTO );

        mockMvc.perform(
                patch( "/api/admin/users/" + UuidCreator.getTimeOrderedEpoch().toString() + "/roles" )
                        .with( csrf() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.message" ).isNotEmpty()
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/Patch updateUserRoles should return 200")
    void updateUserRoles_ShouldReturn200_WhenCalled() throws Exception {
        User saved = userRepository.save( testUser );

        assert saved.getId() != null;

        UserUpdateRolesDTO userUpdateRolesDTO = UserUpdateRolesDTO.builder()
                .roles( List.of( UserRole.SUPERUSER, UserRole.ADMIN ) )
                .build();

        String jsonContent = objectMapper.writeValueAsString( userUpdateRolesDTO );

        mockMvc.perform(
                patch( "/api/admin/users/" + saved.getId().toString() + "/roles" )
                        .with( csrf() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.id" ).value( saved.getId().toString() )
        ).andExpect(
                jsonPath( "$.roles" ).isArray()
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/PATCH updateUserEmployee should return 404 when user does not exist")
    void updateUserEmployee_ShouldReturn404_WhenUserDoesNotExist() throws Exception {
        userRepository.save( testUser );

        UserUpdateEmployeeDTO userUpdateEmployeeDTO = UserUpdateEmployeeDTO.builder()
                .employeeId( null )
                .build();

        String jsonContent = objectMapper.writeValueAsString( userUpdateEmployeeDTO );

        mockMvc.perform(
                patch( "/api/admin/users/" + UuidCreator.getTimeOrderedEpoch().toString() + "/employee" )
                        .with( csrf() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.message" ).isNotEmpty()
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/PATCH updateUserEmployee should return 404 when employee does not exist")
    void updateUserEmployee_ShouldReturn404_WhenEmployeeDoesNotExist() throws Exception {
        userRepository.save( testUser );

        UUID randomId = UuidCreator.getTimeOrderedEpoch();

        UserUpdateEmployeeDTO userUpdateEmployeeDTO = UserUpdateEmployeeDTO.builder()
                .employeeId( randomId.toString() )
                .build();

        String jsonContent = objectMapper.writeValueAsString( userUpdateEmployeeDTO );

        mockMvc.perform(
                patch( "/api/admin/users/" + testUser.getId() + "/employee" )
                        .with( csrf() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.message" ).isNotEmpty()
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/PATCH updateUserEmployee should return 400 when employee is already linked")
    void updateUserEmployee_ShouldReturn400_WhenEmployeeIsAlreadyLinked() throws Exception {
        User saved = userRepository.save( testUser );
        testEmployee.setUser( saved );
        employeeRepository.save( testEmployee );

        assert testEmployee.getId() != null;

        UserUpdateEmployeeDTO userUpdateEmployeeDTO = UserUpdateEmployeeDTO.builder()
                .employeeId( testEmployee.getId().toString() )
                .build();

        String jsonContent = objectMapper.writeValueAsString( userUpdateEmployeeDTO );

        mockMvc.perform(
                patch( "/api/admin/users/" + testUser.getId() + "/employee" )
                        .with( csrf() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.message" ).isNotEmpty()
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/PATCH updateUserEmployee should return 200 and unlink employee")
    void updateUserEmployee_ShouldReturn200AndUnlinkEmployee_WhenCalled() throws Exception {
        User saved = userRepository.save( testUser );
        testEmployee.setUser( saved );
        employeeRepository.save( testEmployee );

        UserUpdateEmployeeDTO userUpdateEmployeeDTO = UserUpdateEmployeeDTO.builder()
                .employeeId( null )
                .build();

        String jsonContent = objectMapper.writeValueAsString( userUpdateEmployeeDTO );

        mockMvc.perform(
                patch( "/api/admin/users/" + testUser.getId() + "/employee" )
                        .with( csrf() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.employee" ).isEmpty()
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.ADMIN })
    @DisplayName("/PATCH updateUserEmployee should return 200 and link employee")
    void updateUserEmployee_ShouldReturn200AndLinkEmployee_WhenCalled() throws Exception {
        userRepository.save( testUser );
        employeeRepository.save( testEmployee );

        assert testEmployee.getId() != null;

        UserUpdateEmployeeDTO userUpdateEmployeeDTO = UserUpdateEmployeeDTO.builder()
                .employeeId( testEmployee.getId().toString() )
                .build();

        String jsonContent = objectMapper.writeValueAsString( userUpdateEmployeeDTO );

        mockMvc.perform(
                patch( "/api/admin/users/" + testUser.getId() + "/employee" )
                        .with( csrf() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.employee" ).isNotEmpty()
        );
    }
}