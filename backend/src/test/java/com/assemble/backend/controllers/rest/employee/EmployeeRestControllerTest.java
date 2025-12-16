/*
 * assemble
 * EmployeeRestControllerTest.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.controllers.rest.employee;

import com.assemble.backend.models.dtos.employee.EmployeeCreateDTO;
import com.assemble.backend.models.dtos.employee.EmployeeUpdateDTO;
import com.assemble.backend.models.dtos.employee.EmployeeUpdateUserDTO;
import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.models.entities.auth.UserRole;
import com.assemble.backend.models.entities.employee.Employee;
import com.assemble.backend.repositories.auth.UserRepository;
import com.assemble.backend.repositories.employee.EmployeeRepository;
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

import java.time.LocalDate;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@DisplayName("EmployeeRestController Integration Test")
class EmployeeRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private IdService idService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private static User testUser;
    private static Employee testEmployee;

    @BeforeEach
    void init() {
        testUser = userRepository.save(
                User.builder()
                        .id( idService.generateIdFor( User.class ) )
                        .firstname( "Test" )
                        .lastname( "User" )
                        .username( "testuser" )
                        .email( "testuser@example.com" )
                        .password( passwordEncoder.encode( "secret" ) )
                        .roles( List.of( UserRole.USER, UserRole.ADMIN, UserRole.SUPERUSER ) )
                        .build()
        );

        testEmployee = Employee.builder()
                .id( idService.generateIdFor( Employee.class ) )
                .firstname( "Max" )
                .lastname( "Mustermann" )
                .email( "testuser@example.com" )
                .build();

    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/GET getAllEmployees should return 200")
    void getAllEmployees_ShouldReturn200_WhenCalled() throws Exception {
        employeeRepository.save( testEmployee );

        mockMvc.perform(
                get( "/api/employees" )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$[0].id" ).isNotEmpty()

        ).andExpect(
                jsonPath( "$[0].lastname" ).value( testEmployee.getLastname() )
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/GET getEmployee should return 404 when employee does not exist")
    void getEmployee_ShouldReturn404_WhenEmployeeDoesNotExist() throws Exception {
        employeeRepository.save( testEmployee );

        mockMvc.perform(
                get( "/api/employees/fake-id" )
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.message" ).isNotEmpty()

        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/GET getEmployee should return 200 when employee exists")
    void getEmployee_ShouldReturn200_WhenEmployeeExists() throws Exception {
        employeeRepository.save( testEmployee );

        mockMvc.perform(
                get( "/api/employees/" + testEmployee.getId() )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.id" ).isNotEmpty()

        ).andExpect(
                jsonPath( "$.lastname" ).value( testEmployee.getLastname() )
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/POST createEmployee should return 400 when dto is invalid")
    void createEmployee_ShouldReturn400_WhenDTOIsInvalid() throws Exception {
        employeeRepository.save( testEmployee );

        EmployeeCreateDTO employeeCreateDTO = EmployeeCreateDTO.builder()
                .firstname( "" )
                .lastname( "" )
                .email( "invalid@" )
                .userId( null )
                .build();

        String jsonContent = objectMapper.writeValueAsString( employeeCreateDTO );

        mockMvc.perform(
                post( "/api/employees" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
                        .with( csrf() )
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.errors" ).isNotEmpty()

        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/POST createEmployee should return 404 when user not found")
    void createEmployee_ShouldReturn404_WhenUserNotFound() throws Exception {
        employeeRepository.save( testEmployee );

        EmployeeCreateDTO employeeCreateDTO = EmployeeCreateDTO.builder()
                .firstname( "Max" )
                .lastname( "Musterperson" )
                .email( "valid@email.com" )
                .userId( "fake-id" )
                .build();

        String jsonContent = objectMapper.writeValueAsString( employeeCreateDTO );

        mockMvc.perform(
                post( "/api/employees" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
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
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/POST createEmployee should return 400 when user already linked")
    void createEmployee_ShouldReturn400_WhenUserAlreadyLinked() throws Exception {
        testEmployee.setUser( testUser );
        employeeRepository.save( testEmployee );

        EmployeeCreateDTO employeeCreateDTO = EmployeeCreateDTO.builder()
                .firstname( "Max" )
                .lastname( "Musterperson" )
                .email( "valid@email.com" )
                .userId( testUser.getId() )
                .build();

        String jsonContent = objectMapper.writeValueAsString( employeeCreateDTO );

        mockMvc.perform(
                post( "/api/employees" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
                        .with( csrf() )
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.message" ).isNotEmpty()

        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/POST createEmployee should return 201 when request body without user is valid")
    void createEmployee_ShouldReturn201_WhenRequestBodyWithoutUserIsValid() throws Exception {
        employeeRepository.save( testEmployee );

        EmployeeCreateDTO employeeCreateDTO = EmployeeCreateDTO.builder()
                .firstname( "Max" )
                .lastname( "Musterperson" )
                .email( "valid@email.com" )
                .userId( null )
                .build();

        String jsonContent = objectMapper.writeValueAsString( employeeCreateDTO );

        mockMvc.perform(
                post( "/api/employees" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
                        .with( csrf() )
        ).andExpect(
                status().isCreated()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.id" ).isNotEmpty()
        ).andExpect(
                jsonPath( "$.user" ).isEmpty()
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/POST createEmployee should return 201 when request body with user is valid")
    void createEmployee_ShouldReturn201_WhenRequestBodyWithUserIsValid() throws Exception {
        employeeRepository.save( testEmployee );

        EmployeeCreateDTO employeeCreateDTO = EmployeeCreateDTO.builder()
                .firstname( "Max" )
                .lastname( "Musterperson" )
                .email( "valid@email.com" )
                .userId( testUser.getId() )
                .build();

        String jsonContent = objectMapper.writeValueAsString( employeeCreateDTO );

        mockMvc.perform(
                post( "/api/employees" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
                        .with( csrf() )
        ).andExpect(
                status().isCreated()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.id" ).isNotEmpty()
        ).andExpect(
                jsonPath( "$.user.firstname" ).value( testUser.getFirstname() )
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/PATCH updateEmployeeUser should return 404 when employee does not exist")
    void updateEmployeeUser_ShouldReturn404_WhenEmployeeDoesNotExist() throws Exception {
        employeeRepository.save( testEmployee );

        EmployeeUpdateUserDTO employeeUpdateUserDTO = EmployeeUpdateUserDTO.builder()
                .userId( "fake-id" )
                .build();

        String jsonContent = objectMapper.writeValueAsString( employeeUpdateUserDTO );

        mockMvc.perform(
                patch( "/api/employees/fake-id/user" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
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
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/PATCH updateEmployeeUser should return 404 when user not found")
    void updateEmployeeUser_ShouldReturn404_WhenUserNotFound() throws Exception {
        employeeRepository.save( testEmployee );

        EmployeeUpdateUserDTO employeeUpdateUserDTO = EmployeeUpdateUserDTO.builder()
                .userId( "fake-id" )
                .build();

        String jsonContent = objectMapper.writeValueAsString( employeeUpdateUserDTO );

        mockMvc.perform(
                patch( "/api/employees/" + testEmployee.getId() + "/user" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
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
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/PATCH updateEmployeeUser should return 400 when user is already linked")
    void updateEmployeeUser_ShouldReturn400_WhenUserIsAlreadyLinked() throws Exception {
        testEmployee.setUser( testUser );
        employeeRepository.save( testEmployee );

        EmployeeUpdateUserDTO employeeUpdateUserDTO = EmployeeUpdateUserDTO.builder()
                .userId( testUser.getId() )
                .build();

        String jsonContent = objectMapper.writeValueAsString( employeeUpdateUserDTO );

        mockMvc.perform(
                patch( "/api/employees/" + testEmployee.getId() + "/user" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
                        .with( csrf() )
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.message" ).isNotEmpty()
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/PATCH updateEmployeeUser should return 200 and user should be null")
    void updateEmployeeUser_ShouldReturn200AndUserShouldBeNull_WhenCalled() throws Exception {
        employeeRepository.save( testEmployee );

        EmployeeUpdateUserDTO employeeUpdateUserDTO = EmployeeUpdateUserDTO.builder()
                .userId( null )
                .build();

        String jsonContent = objectMapper.writeValueAsString( employeeUpdateUserDTO );

        mockMvc.perform(
                patch( "/api/employees/" + testEmployee.getId() + "/user" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
                        .with( csrf() )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.user" ).isEmpty()
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/PATCH updateEmployeeUser should return 200 and user should be connected")
    void updateEmployeeUser_ShouldReturn200AndUserShouldBeConnected_WhenCalled() throws Exception {
        employeeRepository.save( testEmployee );

        EmployeeUpdateUserDTO employeeUpdateUserDTO = EmployeeUpdateUserDTO.builder()
                .userId( testUser.getId() )
                .build();

        String jsonContent = objectMapper.writeValueAsString( employeeUpdateUserDTO );

        mockMvc.perform(
                patch( "/api/employees/" + testEmployee.getId() + "/user" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
                        .with( csrf() )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.user.firstname" ).value( testUser.getFirstname() )
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/PATCH updateEmployee should return 400 when dto is invalid")
    void updateEmployee_ShouldReturn400_WhenDTOIsInvalid() throws Exception {
        employeeRepository.save( testEmployee );

        EmployeeUpdateDTO employeeUpdateDTO = EmployeeUpdateDTO.builder()
                .firstname( "" )
                .lastname( "" )
                .email( "invalid@" )
                .build();

        String jsonContent = objectMapper.writeValueAsString( employeeUpdateDTO );

        mockMvc.perform(
                patch( "/api/employees/" + testEmployee.getId() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
                        .with( csrf() )
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.errors" ).isNotEmpty()
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/PATCH updateEmployee should return 404 when employee does not exist")
    void updateEmployee_ShouldReturn404_WhenEmployeeDoesNotExist() throws Exception {
        employeeRepository.save( testEmployee );

        EmployeeUpdateDTO employeeUpdateDTO = EmployeeUpdateDTO.builder()
                .firstname( "max" )
                .lastname( "musterperson" )
                .email( "valid@email.com" )
                .build();

        String jsonContent = objectMapper.writeValueAsString( employeeUpdateDTO );

        mockMvc.perform(
                patch( "/api/employees/fake-id" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
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
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/PATCH updateEmployee should return 200 when request body is valid")
    void updateEmployee_ShouldReturn200_WhenRequestBodyIsValid() throws Exception {
        employeeRepository.save( testEmployee );

        EmployeeUpdateDTO employeeUpdateDTO = EmployeeUpdateDTO.builder()
                .firstname( "max" )
                .lastname( "musterperson" )
                .email( "valid@email.com" )
                .build();

        String jsonContent = objectMapper.writeValueAsString( employeeUpdateDTO );

        mockMvc.perform(
                patch( "/api/employees/" + testEmployee.getId() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
                        .with( csrf() )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.firstname" ).value( employeeUpdateDTO.getFirstname() )
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/DELETE deleteEmployee should return 404 when employee does not exist")
    void deleteEmployee_ShouldReturn404_WhenEmployeeDoesNotExist() throws Exception {
        employeeRepository.save( testEmployee );

        mockMvc.perform(
                delete( "/api/employees/fake-id" )
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
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/DELETE deleteEmployee should return 204 when employee does exist")
    void deleteEmployee_ShouldReturn204_WhenEmployeeDoesExist() throws Exception {
        employeeRepository.save( testEmployee );

        mockMvc.perform(
                delete( "/api/employees/" + testEmployee.getId() )
                        .with( csrf() )
        ).andExpect(
                status().isNoContent()
        );
    }

}