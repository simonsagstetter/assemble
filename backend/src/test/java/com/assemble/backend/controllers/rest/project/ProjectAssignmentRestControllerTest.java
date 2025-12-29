/*
 * assemble
 * ProjectAssignmentRestControllerTest.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.controllers.rest.project;

import com.assemble.backend.models.dtos.project.ProjectAssignmentCreateDTO;
import com.assemble.backend.models.dtos.project.ProjectAssignmentDeleteDTO;
import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.models.entities.auth.UserRole;
import com.assemble.backend.models.entities.employee.Employee;
import com.assemble.backend.models.entities.project.Project;
import com.assemble.backend.models.entities.project.ProjectAssignment;
import com.assemble.backend.models.entities.project.ProjectStage;
import com.assemble.backend.models.entities.project.ProjectType;
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

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@DisplayName("ProjectAssignmentRestController Integration Test")
class ProjectAssignmentRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectAssignmentRepository projectAssignmentRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static Project testProject;
    private static Employee testEmployee;
    private static ProjectAssignment testProjectAssignment;

    @BeforeEach
    void init() {
        testProject = projectRepository
                .save(
                        Project.builder()
                                .name( "Test Project" )
                                .description( "Test Project Description" )
                                .category( "Maintanance" )
                                .stage( ProjectStage.PROPOSAL )
                                .type( ProjectType.EXTERNAL )
                                .build()
                );

        User testUser = userRepository.save(
                User.builder()
                        .firstname( "Test" )
                        .lastname( "User" )
                        .username( "testuser" )
                        .email( "testuser@example.com" )
                        .password( passwordEncoder.encode( "secret" ) )
                        .roles( List.of( UserRole.USER, UserRole.ADMIN, UserRole.SUPERUSER ) )
                        .build()
        );

        testEmployee = employeeRepository.save(
                Employee.builder()
                        .firstname( "Max" )
                        .lastname( "Mustermann" )
                        .email( "testuser@example.com" )
                        .user( testUser )
                        .build()
        );

        testProjectAssignment = ProjectAssignment.builder()
                .project( testProject )
                .employee( testEmployee )
                .active( true )
                .hourlyRate( BigDecimal.valueOf( 45 ) )
                .build();
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/GET getAllProjectAssignmentsByProjectId should return status code 200")
    void getAllProjectAssignmentsByProjectId_ShouldReturnStatusCode200_WhenCalled() throws Exception {
        ProjectAssignment projectAssignment = projectAssignmentRepository.save( testProjectAssignment );
        assert projectAssignment.getId() != null;
        assert testProject.getId() != null;
        assert testEmployee.getId() != null;

        mockMvc.perform(
                        get( "/api/projectassignments/project/" + testProject.getId().toString() )
                ).andExpect(
                        status().isOk()
                ).andExpect(
                        content().contentType( MediaType.APPLICATION_JSON )
                )
                .andExpect(
                        jsonPath( "$[0].id" ).value( projectAssignment.getId().toString() )
                )
                .andExpect(
                        jsonPath( "$[0].employee.id" ).value( testEmployee.getId().toString() )
                )
                .andExpect(
                        jsonPath( "$[0].project.id" ).value( testProject.getId().toString() )
                )
                .andExpect(
                        jsonPath( "$[0].hourlyRate" ).value( testProjectAssignment.getHourlyRate().doubleValue() )
                );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/GET getAllProjectAssignmentsByEmployeeId should return status code 200")
    void getAllProjectAssignmentsByEmployeeId_ShouldReturnStatusCode200_WhenCalled() throws Exception {
        ProjectAssignment projectAssignment = projectAssignmentRepository.save( testProjectAssignment );
        assert projectAssignment.getId() != null;
        assert testProject.getId() != null;
        assert testEmployee.getId() != null;

        mockMvc.perform(
                        get( "/api/projectassignments/employee/" + testEmployee.getId().toString() )
                ).andExpect(
                        status().isOk()
                ).andExpect(
                        content().contentType( MediaType.APPLICATION_JSON )
                )
                .andExpect(
                        jsonPath( "$[0].id" ).value( projectAssignment.getId().toString() )
                )
                .andExpect(
                        jsonPath( "$[0].employee.id" ).value( testEmployee.getId().toString() )
                )
                .andExpect(
                        jsonPath( "$[0].project.id" ).value( testProject.getId().toString() )
                )
                .andExpect(
                        jsonPath( "$[0].hourlyRate" ).value( testProjectAssignment.getHourlyRate().doubleValue() )
                );
    }


    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/POST createProjectAssignments should return status code 400 when request body is invalid")
    void createProjectAssignments_ShouldReturnStatusCode400_WhenRequestBodyIsInvalid() throws Exception {
        List<ProjectAssignmentCreateDTO> dtoList = List.of(
                ProjectAssignmentCreateDTO.builder()
                        .projectId( "" )
                        .employeeId( "" )
                        .hourlyRate( BigDecimal.valueOf( 50 ) )
                        .build()
        );

        String jsonContent = objectMapper.writeValueAsString( dtoList );

        mockMvc.perform(
                post( "/api/projectassignments" )
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
    @DisplayName("/POST createProjectAssignments should return status code 404 when employee not found")
    void createProjectAssignments_ShouldReturnStatusCode404_WhenEmployeeNotFound() throws Exception {
        assert testProject.getId() != null;
        List<ProjectAssignmentCreateDTO> dtoList = List.of(
                ProjectAssignmentCreateDTO.builder()
                        .employeeId( UuidCreator.getTimeOrderedEpoch().toString() )
                        .projectId( testProject.getId().toString() )
                        .hourlyRate( BigDecimal.valueOf( 50 ) )
                        .build()
        );

        String jsonContent = objectMapper.writeValueAsString( dtoList );

        mockMvc.perform(
                post( "/api/projectassignments" )
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
    @DisplayName("/POST createProjectAssignments should return status code 404 when project not found")
    void createProjectAssignments_ShouldReturnStatusCode404_WhenProjectNotFound() throws Exception {
        assert testEmployee.getId() != null;
        List<ProjectAssignmentCreateDTO> dtoList = List.of(
                ProjectAssignmentCreateDTO.builder()
                        .projectId( UuidCreator.getTimeOrderedEpoch().toString() )
                        .employeeId( testEmployee.getId().toString() )
                        .hourlyRate( BigDecimal.valueOf( 50 ) )
                        .build()
        );

        String jsonContent = objectMapper.writeValueAsString( dtoList );

        mockMvc.perform(
                post( "/api/projectassignments" )
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
    @DisplayName("/POST createProjectAssignments should return status code 201 when request body is valid")
    void createProjectAssignments_ShouldReturnStatusCode201_WhenRequestBodyIsValid() throws Exception {
        assert testEmployee.getId() != null;
        assert testProject.getId() != null;
        List<ProjectAssignmentCreateDTO> dtoList = List.of(
                ProjectAssignmentCreateDTO.builder()
                        .projectId( testProject.getId().toString() )
                        .employeeId( testEmployee.getId().toString() )
                        .hourlyRate( BigDecimal.valueOf( 50 ) )
                        .build()
        );

        String jsonContent = objectMapper.writeValueAsString( dtoList );

        mockMvc.perform(
                        post( "/api/projectassignments" )
                                .contentType( MediaType.APPLICATION_JSON )
                                .content( jsonContent )
                                .with( csrf() )
                ).andExpect(
                        status().isCreated()
                ).andExpect(
                        content().contentType( MediaType.APPLICATION_JSON )
                ).andExpect(
                        jsonPath( "$[0].id" ).isNotEmpty()
                )
                .andExpect(
                        jsonPath( "$[0].employee.id" ).value( testEmployee.getId().toString() )
                )
                .andExpect(
                        jsonPath( "$[0].project.id" ).value( testProject.getId().toString() )
                )
                .andExpect(
                        jsonPath( "$[0].hourlyRate" ).value( dtoList.getFirst().getHourlyRate().doubleValue() )
                );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/DELETE deleteProjectAssignmentsByIds should return status code 400 when request body is invalid")
    void deleteProjectAssignmentsByIds_ShouldReturnStatusCode400_WhenRequestBodyIsInvalid() throws Exception {
        ProjectAssignmentDeleteDTO dto = ProjectAssignmentDeleteDTO.builder()
                .ids( List.of() )
                .build();

        String jsonContent = objectMapper.writeValueAsString( dto );

        mockMvc.perform(
                delete( "/api/projectassignments" )
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
    @DisplayName("/DELETE deleteProjectAssignmentsByIds should return status code 404 when ProjectAssignment not found")
    void deleteProjectAssignmentsByIds_ShouldReturnStatusCode404_WhenProjectAssignmentNotFound() throws Exception {
        ProjectAssignmentDeleteDTO dto = ProjectAssignmentDeleteDTO.builder()
                .ids( List.of( UuidCreator.getTimeOrderedEpoch().toString() ) )
                .build();

        String jsonContent = objectMapper.writeValueAsString( dto );

        mockMvc.perform(
                delete( "/api/projectassignments" )
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
    @DisplayName("/DELETE deleteProjectAssignmentsByIds should return status code 204 when request body is valid")
    void deleteProjectAssignmentsByIds_ShouldReturnStatusCode204_WhenRequestBodyIsValid() throws Exception {
        ProjectAssignment projectAssignment = projectAssignmentRepository.save( testProjectAssignment );
        assert projectAssignment.getId() != null;
        ProjectAssignmentDeleteDTO dto = ProjectAssignmentDeleteDTO.builder()
                .ids( List.of( projectAssignment.getId().toString() ) )
                .build();

        String jsonContent = objectMapper.writeValueAsString( dto );

        mockMvc.perform(
                delete( "/api/projectassignments" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
                        .with( csrf() )
        ).andExpect(
                status().isNoContent()
        );
    }
}