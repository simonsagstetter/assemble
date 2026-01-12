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
import com.assemble.backend.models.entities.project.*;
import com.assemble.backend.repositories.auth.UserRepository;
import com.assemble.backend.repositories.employee.EmployeeRepository;
import com.assemble.backend.repositories.project.ProjectAssignmentRepository;
import com.assemble.backend.repositories.project.ProjectRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@DisplayName("EmployeeRestController Integration Test")
class EmployeeRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectAssignmentRepository projectAssignmentRepository;

    @Autowired
    private ProjectRepository projectRepository;

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
                        .firstname( "Test" )
                        .lastname( "User" )
                        .username( "testuser" )
                        .email( "testuser@example.com" )
                        .password( passwordEncoder.encode( "secret" ) )
                        .roles( List.of( UserRole.USER, UserRole.ADMIN, UserRole.SUPERUSER ) )
                        .build()
        );

        testEmployee = Employee.builder()
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

        UUID randomId = UuidCreator.getTimeOrderedEpoch();

        mockMvc.perform(
                get( "/api/employees/" + randomId.toString() )
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
        Employee employee = employeeRepository.save( testEmployee );

        assertNotNull( employee.getId() );

        mockMvc.perform(
                get( "/api/employees/" + employee.getId().toString() )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.id" ).isNotEmpty()

        ).andExpect(
                jsonPath( "$.lastname" ).value( employee.getLastname() )
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/GET searchUnlinkedEmployees should return 200")
    void searchUnlinkedEmployees_ShouldReturn200_WhenCalled() throws Exception {
        employeeRepository.save( testEmployee );

        mockMvc.perform(
                get( "/api/employees/search/" + testEmployee.getFirstname() )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$[0].id" ).isNotEmpty()

        ).andExpect(
                jsonPath( "$[0].fullname" ).value( testEmployee.getFullname() )
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/GET searchAllEmployees should return 200")
    void searchAllEmployeess_ShouldReturn200_WhenCalled() throws Exception {
        employeeRepository.save( testEmployee );

        mockMvc.perform(
                get( "/api/employees/search-all/" + testEmployee.getFirstname() )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$[0].id" ).isNotEmpty()

        ).andExpect(
                jsonPath( "$[0].fullname" ).value( testEmployee.getFullname() )
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
                .userId( UuidCreator.getTimeOrderedEpoch().toString() )
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
        User user = userRepository.save( testUser );
        testEmployee.setUser( user );
        employeeRepository.save( testEmployee );

        assertNotNull( user.getId() );

        EmployeeCreateDTO employeeCreateDTO = EmployeeCreateDTO.builder()
                .firstname( "Max" )
                .lastname( "Musterperson" )
                .email( "valid@email.com" )
                .userId( user.getId().toString() )
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
        User user = userRepository.save( testUser );
        employeeRepository.save( testEmployee );

        assertNotNull( user.getId() );

        EmployeeCreateDTO employeeCreateDTO = EmployeeCreateDTO.builder()
                .firstname( "Max" )
                .lastname( "Musterperson" )
                .email( "valid@email.com" )
                .userId( user.getId().toString() )
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

        UUID randomId = UuidCreator.getTimeOrderedEpoch();

        EmployeeUpdateUserDTO employeeUpdateUserDTO = EmployeeUpdateUserDTO.builder()
                .userId( randomId.toString() )
                .build();

        String jsonContent = objectMapper.writeValueAsString( employeeUpdateUserDTO );

        mockMvc.perform(
                patch( "/api/employees/" + randomId + "/user" )
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
        Employee employee = employeeRepository.save( testEmployee );

        assertNotNull( employee.getId() );

        UUID randomId = UuidCreator.getTimeOrderedEpoch();

        EmployeeUpdateUserDTO employeeUpdateUserDTO = EmployeeUpdateUserDTO.builder()
                .userId( randomId.toString() )
                .build();

        String jsonContent = objectMapper.writeValueAsString( employeeUpdateUserDTO );


        mockMvc.perform(
                patch( "/api/employees/" + employee.getId().toString() + "/user" )
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
        User user = userRepository.save( testUser );
        assertNotNull( user.getId() );

        testEmployee.setUser( user );
        Employee employee = employeeRepository.save( testEmployee );

        assertNotNull( employee.getId() );

        EmployeeUpdateUserDTO employeeUpdateUserDTO = EmployeeUpdateUserDTO.builder()
                .userId( user.getId().toString() )
                .build();

        String jsonContent = objectMapper.writeValueAsString( employeeUpdateUserDTO );

        mockMvc.perform(
                patch( "/api/employees/" + employee.getId().toString() + "/user" )
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
        Employee employee = employeeRepository.save( testEmployee );

        assertNotNull( employee.getId() );

        EmployeeUpdateUserDTO employeeUpdateUserDTO = EmployeeUpdateUserDTO.builder()
                .userId( null )
                .build();

        String jsonContent = objectMapper.writeValueAsString( employeeUpdateUserDTO );

        mockMvc.perform(
                patch( "/api/employees/" + employee.getId().toString() + "/user" )
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
        User user = userRepository.save( testUser );
        Employee employee = employeeRepository.save( testEmployee );

        assertNotNull( employee.getId() );
        assertNotNull( user.getId() );

        EmployeeUpdateUserDTO employeeUpdateUserDTO = EmployeeUpdateUserDTO.builder()
                .userId( user.getId().toString() )
                .build();

        String jsonContent = objectMapper.writeValueAsString( employeeUpdateUserDTO );

        mockMvc.perform(
                patch( "/api/employees/" + employee.getId().toString() + "/user" )
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
        Employee employee = employeeRepository.save( testEmployee );

        assertNotNull( employee.getId() );

        EmployeeUpdateDTO employeeUpdateDTO = EmployeeUpdateDTO.builder()
                .firstname( "" )
                .lastname( "" )
                .email( "invalid@" )
                .build();

        String jsonContent = objectMapper.writeValueAsString( employeeUpdateDTO );

        mockMvc.perform(
                patch( "/api/employees/" + employee.getId().toString() )
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

        UUID randomId = UuidCreator.getTimeOrderedEpoch();

        EmployeeUpdateDTO employeeUpdateDTO = EmployeeUpdateDTO.builder()
                .firstname( "max" )
                .lastname( "musterperson" )
                .email( "valid@email.com" )
                .build();

        String jsonContent = objectMapper.writeValueAsString( employeeUpdateDTO );

        mockMvc.perform(
                patch( "/api/employees/" + randomId.toString() )
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
        Employee employee = employeeRepository.save( testEmployee );

        assertNotNull( employee.getId() );

        EmployeeUpdateDTO employeeUpdateDTO = EmployeeUpdateDTO.builder()
                .firstname( "max" )
                .lastname( "musterperson" )
                .email( "valid@email.com" )
                .build();

        String jsonContent = objectMapper.writeValueAsString( employeeUpdateDTO );

        mockMvc.perform(
                patch( "/api/employees/" + employee.getId().toString() )
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

        UUID randomId = UuidCreator.getTimeOrderedEpoch();

        mockMvc.perform(
                delete( "/api/employees/" + randomId.toString() )
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
        Employee employee = employeeRepository.save( testEmployee );

        assertNotNull( employee.getId() );

        mockMvc.perform(
                delete( "/api/employees/" + employee.getId().toString() )
                        .with( csrf() )
        ).andExpect(
                status().isNoContent()
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/DELETE deleteEmployee should return 204 and delete related assignments when employee does exist")
    void deleteEmplyoee_ShouldReturn204AndDeleteRelatedAssignments_WhenEmployeeDoesExist() throws Exception {
        Employee employee = employeeRepository.save( testEmployee );
        Project project = projectRepository.save(
                Project.builder()
                        .name( "TestProject" )
                        .description( "TestProjectDescription" )
                        .category( "TestCategory" )
                        .stage( ProjectStage.PROPOSAL )
                        .type( ProjectType.EXTERNAL )
                        .color( ProjectColor.PURPLE )
                        .build()
        );
        projectAssignmentRepository.save(
                ProjectAssignment.builder()
                        .employee( employee )
                        .project( project )
                        .build()
        );

        assertNotNull( employee.getId() );

        mockMvc.perform(
                delete( "/api/employees/" + employee.getId().toString() )
                        .with( csrf() )
        ).andExpect(
                status().isNoContent()
        );

        assert projectAssignmentRepository.count() == 0;
    }

}