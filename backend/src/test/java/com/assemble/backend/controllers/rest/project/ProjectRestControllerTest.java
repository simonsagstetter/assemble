/*
 * assemble
 * ProjectRestControllerTest.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.controllers.rest.project;

import com.assemble.backend.models.dtos.project.ProjectCreateDTO;
import com.assemble.backend.models.entities.auth.UserRole;
import com.assemble.backend.models.entities.employee.Employee;
import com.assemble.backend.models.entities.project.*;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DisplayName("ProjectRestController Integration Test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class ProjectRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProjectAssignmentRepository projectAssignmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static Project testProject;

    @BeforeEach
    void init() {
        testProject = Project.builder()
                .name( "Test Project" )
                .description( "Test Project Description" )
                .category( "Maintanance" )
                .stage( ProjectStage.PROPOSAL )
                .type( ProjectType.EXTERNAL )
                .color( ProjectColor.PURPLE )
                .build();
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/GET getAllProjects should return status code 200 and a empty list")
    void getAllProjects_ShouldReturnStatusCode200AndAEmptyList_WhenCalled() throws Exception {

        mockMvc.perform(
                get( "/api/projects" )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                content().json( """
                            []
                        """ )
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/GET getAllProjects should return status code 200 and a project list")
    void getAllProjects_ShouldReturnStatusCode200AndAProjectList_WhenCalled() throws Exception {
        Project project = projectRepository.save( testProject );

        assert project.getId() != null;

        mockMvc.perform(
                        get( "/api/projects" )
                ).andExpect(
                        status().isOk()
                ).andExpect(
                        content().contentType( MediaType.APPLICATION_JSON )
                ).andExpect(
                        jsonPath( "$[0].id" ).value( project.getId().toString() )
                ).andExpect(
                        jsonPath( "$[0].no" ).value( project.getNo() )
                )
                .andExpect(
                        jsonPath( "$[0].name" ).value( project.getName() )
                );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/GET getProjectById should return status code 404 when project does not exist in db")
    void getProjectById_ShouldReturnStatusCode404_WhenProjectDoesNotExistInDB() throws Exception {

        mockMvc.perform(
                get( "/api/projects/" + UuidCreator.getTimeOrderedEpoch().toString() )
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
    @DisplayName("/GET getProjectById should return status code 200 when project does exist in db")
    void getProjectById_ShouldReturnStatusCode200_WhenProjectDoesExistInDB() throws Exception {
        Project project = projectRepository.save( testProject );
        assert project.getId() != null;
        mockMvc.perform(
                        get( "/api/projects/" + project.getId().toString() )
                ).andExpect(
                        status().isOk()
                ).andExpect(
                        content().contentType( MediaType.APPLICATION_JSON )
                ).andExpect(
                        jsonPath( "$.id" ).value( project.getId().toString() )
                ).andExpect(
                        jsonPath( "$.no" ).value( project.getNo() )
                )
                .andExpect(
                        jsonPath( "$.name" ).value( project.getName() )
                );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/GET searchAllProjects should return status code 200 when project does exist in db")
    void searchAllProjects_ShouldReturnStatusCode200_WhenProjectDoesExistInDB() throws Exception {
        Project project = projectRepository.save( testProject );
        assert project.getId() != null;
        mockMvc.perform(
                        get( "/api/projects/search/{searchTerm}", project.getName() )
                ).andExpect(
                        status().isOk()
                ).andExpect(
                        content().contentType( MediaType.APPLICATION_JSON )
                ).andExpect(
                        jsonPath( "$[0].id" ).value( project.getId().toString() )
                ).andExpect(
                        jsonPath( "$[0].no" ).value( project.getNo() )
                )
                .andExpect(
                        jsonPath( "$[0].name" ).value( project.getName() )
                );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/POST createProject should return status code 400 when request body is invalid")
    void createProject_ShouldReturnStatusCode400_WhenRequestBodyIsInvalid() throws Exception {
        ProjectCreateDTO invalidCreateDTO = ProjectCreateDTO.builder()
                .name( "     " )
                .description( "Test Project Description" )
                .category( "Maintanance" )
                .stage( ProjectStage.PROPOSAL )
                .type( ProjectType.EXTERNAL )
                .active( true )
                .build();

        String jsonContent = objectMapper.writeValueAsString( invalidCreateDTO );

        mockMvc.perform(
                post( "/api/projects" )
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
    @DisplayName("/POST createProject should return status code 409 when project name is not unqiue")
    void createProject_ShouldReturnStatusCode409_WhenProjectNameIsNotUnique() throws Exception {
        Project project = projectRepository.save( testProject );

        ProjectCreateDTO invalidCreateDTO = ProjectCreateDTO.builder()
                .name( project.getName() )
                .description( "Test Project Description" )
                .category( "Maintanance" )
                .stage( ProjectStage.PROPOSAL )
                .type( ProjectType.EXTERNAL )
                .active( true )
                .build();

        String jsonContent = objectMapper.writeValueAsString( invalidCreateDTO );

        mockMvc.perform(
                post( "/api/projects" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
                        .with( csrf() )
        ).andExpect(
                status().isConflict()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.message" ).isNotEmpty()
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/POST createProject should return status code 201 when request body is valid")
    void createProject_ShouldReturnStatusCode201_WhenRequestBodyIsValid() throws Exception {
        ProjectCreateDTO validCreateDTO = ProjectCreateDTO.builder()
                .name( "Test Project 2" )
                .description( "Test Project Description" )
                .category( "Maintanance" )
                .stage( ProjectStage.PROPOSAL )
                .type( ProjectType.EXTERNAL )
                .active( false )
                .build();

        String jsonContent = objectMapper.writeValueAsString( validCreateDTO );

        mockMvc.perform(
                        post( "/api/projects" )
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
                        jsonPath( "$.no" ).isNotEmpty()
                )
                .andExpect(
                        jsonPath( "$.name" ).value( validCreateDTO.getName() )
                ).andExpect(
                        jsonPath( "$.description" ).value( validCreateDTO.getDescription() )
                ).andExpect(
                        jsonPath( "$.category" ).value( validCreateDTO.getCategory() )
                ).andExpect(
                        jsonPath( "$.stage" ).value( validCreateDTO.getStage().toString() )
                ).andExpect(
                        jsonPath( "$.type" ).value( validCreateDTO.getType().toString() )
                ).andExpect(
                        jsonPath( "$.active" ).value( validCreateDTO.isActive() )
                );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/DELETE should return status code 404 when project does not exist in db")
    void deleteProjectById_ShouldReturnStatusCode404_WhenProjectDoesNotExistInDB() throws Exception {
        mockMvc.perform(
                delete( "/api/projects/" + UuidCreator.getTimeOrderedEpoch().toString() )
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
    @DisplayName("/DELETE should return status code 204 when project does exist in db")
    void deleteProjectById_ShouldReturnStatusCode204_WhenProjectDoesExistInDB() throws Exception {
        Project project = projectRepository.save( testProject );
        assert project.getId() != null;
        mockMvc.perform(
                delete( "/api/projects/" + project.getId().toString() )
                        .with( csrf() )
        ).andExpect(
                status().isNoContent()
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/DELETE should return status code 204 and delete related assignments when project does exist in db")
    void deleteProjectById_ShouldReturnStatusCode204AndDeleteRelatedAssignments_WhenProjectDoesExistInDB() throws Exception {
        Project project = projectRepository.save( testProject );
        Employee employee = employeeRepository.save(
                Employee.builder()
                        .firstname( "Max" )
                        .lastname( "Mustermann" )
                        .email( "testuser@example.com" )
                        .build()
        );
        projectAssignmentRepository.save(
                ProjectAssignment.builder()
                        .employee( employee )
                        .project( project )
                        .build()
        );

        assert project.getId() != null;
        mockMvc.perform(
                delete( "/api/projects/" + project.getId().toString() )
                        .with( csrf() )
        ).andExpect(
                status().isNoContent()
        );

        assert projectAssignmentRepository.count() == 0;
    }

}