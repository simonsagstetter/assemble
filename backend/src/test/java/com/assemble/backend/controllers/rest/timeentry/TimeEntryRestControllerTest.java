/*
 * assemble
 * TimeEntryRestControllerTest.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.controllers.rest.timeentry;

import com.assemble.backend.models.dtos.timeentry.TimeEntryCreateDTO;
import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.models.entities.auth.UserRole;
import com.assemble.backend.models.entities.employee.Employee;
import com.assemble.backend.models.entities.project.Project;
import com.assemble.backend.models.entities.project.ProjectStage;
import com.assemble.backend.models.entities.project.ProjectType;
import com.assemble.backend.models.entities.timeentry.TimeEntry;
import com.assemble.backend.repositories.auth.UserRepository;
import com.assemble.backend.repositories.employee.EmployeeRepository;
import com.assemble.backend.repositories.project.ProjectRepository;
import com.assemble.backend.repositories.timeentry.TimeEntryRepository;
import com.assemble.backend.services.timeentry.TimeEntryService;
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
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class TimeEntryRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TimeEntryService timeEntryService;

    @Autowired
    private TimeEntryRepository timeEntryRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static User testUser;
    private static Employee testEmployee;
    private static Project testProject;
    private static TimeEntry testTimeEntry;
    private static final UUID notExistingId = UuidCreator.getTimeOrderedEpoch();

    @BeforeEach
    void setup() {
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

        testEmployee = employeeRepository.save(
                Employee.builder()
                        .firstname( "Max" )
                        .lastname( "Mustermann" )
                        .email( "testuser@example.com" )
                        .user( testUser )
                        .build()
        );

        testProject = projectRepository.save(
                Project.builder()
                        .name( "Test Project" )
                        .description( "Test Project Description" )
                        .category( "Maintanance" )
                        .stage( ProjectStage.PROPOSAL )
                        .type( ProjectType.EXTERNAL )
                        .build()
        );

        testTimeEntry = TimeEntry.builder()
                .description( "Test Time Entry" )
                .project( testProject )
                .employee( testEmployee )
                .rate( BigDecimal.valueOf( 45 ) )
                .total( BigDecimal.valueOf( 450 ) )
                .totalInternal( BigDecimal.valueOf( 300 ) )
                .build();

    }

    @Test
    @WithMockCustomUser
    @DisplayName("/GET getAllTimeEntries should return status code 200 and a empty list")
    void getAllTimeEntries_ShouldReturnStatusCode200AndAEmptyList() throws Exception {
        mockMvc.perform(
                get( "/api/timeentries" )
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
    @WithMockCustomUser
    @DisplayName("/GET getAllTimeEntries should return status code 200 and a timeentry list")
    void getAllTimeEntries_ShouldReturnStatusCode200AndATimeEntryList() throws Exception {
        TimeEntry timeEntry = timeEntryRepository.save( testTimeEntry );
        assert timeEntry.getId() != null;
        mockMvc.perform(
                get( "/api/timeentries" )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$[0].id" ).value( timeEntry.getId().toString() )
        );
    }

    @Test
    @WithMockCustomUser
    @DisplayName("/GET getAllTimeEntriesByProjectId should return status code 200 and a empty list")
    void getAllTimeEntriesByProjectId_ShouldReturnStatusCode200AndAEmptyList() throws Exception {
        mockMvc.perform(
                get( "/api/timeentries/project/" + UuidCreator.getTimeOrderedEpoch().toString() )
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
    @WithMockCustomUser
    @DisplayName("/GET getAllTimeEntriesByProjectId should return status code 200 and a timeentry list")
    void getAllTimeEntriesByProjectId_ShouldReturnStatusCode200AndATimeEntryList() throws Exception {
        projectRepository.save( testProject );
        TimeEntry timeEntry = timeEntryRepository.save( testTimeEntry );
        assert timeEntry.getProject().getId() != null;
        assert timeEntry.getId() != null;
        mockMvc.perform(
                get( "/api/timeentries/project/" + timeEntry.getProject().getId().toString() )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$[0].id" ).value( timeEntry.getId().toString() )
        ).andExpect(
                jsonPath( "$[0].project.id" ).value( timeEntry.getProject().getId().toString() )
        );
    }

    @Test
    @WithMockCustomUser
    @DisplayName("/GET getAllTimeEntriesByEmployeeId should return status code 200 and a empty list")
    void getAllTimeEntriesByEmployeeId_ShouldReturnStatusCode200AndAEmptyList() throws Exception {
        mockMvc.perform(
                get( "/api/timeentries/employee/" + UuidCreator.getTimeOrderedEpoch().toString() )
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
    @WithMockCustomUser
    @DisplayName("/GET getAllTimeEntriesByEmployeeId should return status code 200 and a timeentry list")
    void getAllTimeEntriesByEmployeeId_ShouldReturnStatusCode200AndATimeEntryList() throws Exception {
        employeeRepository.save( testEmployee );
        TimeEntry timeEntry = timeEntryRepository.save( testTimeEntry );
        assert timeEntry.getEmployee().getId() != null;
        assert timeEntry.getId() != null;
        mockMvc.perform(
                get( "/api/timeentries/employee/" + timeEntry.getEmployee().getId().toString() )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$[0].id" ).value( timeEntry.getId().toString() )
        ).andExpect(
                jsonPath( "$[0].employee.id" ).value( timeEntry.getEmployee().getId().toString() )
        );
    }

    @Test
    @WithMockCustomUser
    @DisplayName("/GET getTimeEntryById should return status code 404 when time entry not found")
    void getTimeEntryById_ShouldReturnStatusCode404_WhenTimeEntryNotFound() throws Exception {
        mockMvc.perform(
                        get( "/api/timeentries/" + UuidCreator.getTimeOrderedEpoch().toString() )
                )
                .andExpect(
                        status().isNotFound()
                ).andExpect(
                        content().contentType( MediaType.APPLICATION_JSON )
                ).andExpect(
                        jsonPath( "$.message" ).isNotEmpty()
                );
    }

    @Test
    @WithMockCustomUser
    @DisplayName("/GET getTimeEntryById should return status code 200 when time entry was found")
    void getTimeEntryById_ShouldReturnStatusCode200_WhenTimeEntryWasFound() throws Exception {
        TimeEntry timeEntry = timeEntryRepository.save( testTimeEntry );
        assert timeEntry.getId() != null;
        mockMvc.perform(
                        get( "/api/timeentries/" + timeEntry.getId().toString() )
                )
                .andExpect(
                        status().isOk()
                ).andExpect(
                        content().contentType( MediaType.APPLICATION_JSON )
                ).andExpect(
                        jsonPath( "$.id" ).value( timeEntry.getId().toString() )
                );
    }

    @Test
    @WithMockCustomUser
    @DisplayName("/POST createTimeEntry should return status code 404 when project not found")
    void createTimeEntry_ShouldReturnStatusCode404_WhenProjectNotFound() throws Exception {
        TimeEntryCreateDTO timeEntryCreateDTO = TimeEntryCreateDTO.builder()
                .projectId( notExistingId.toString() )
                .employeeId( notExistingId.toString() )
                .description( "Test description" )
                .totalTime( Duration.ofHours( 8 ) )
                .build();

        String jsonContent = objectMapper.writeValueAsString( timeEntryCreateDTO );

        mockMvc.perform(
                post( "/api/timeentries" )
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
    @WithMockCustomUser
    @DisplayName("/POST createTimeEntry should return status code 404 when employee not found")
    void createTimeEntry_ShouldReturnStatusCode404_WhenEmployeeNotFound() throws Exception {
        Project project = projectRepository.save( testProject );
        assert project.getId() != null;

        TimeEntryCreateDTO timeEntryCreateDTO = TimeEntryCreateDTO.builder()
                .projectId( project.getId().toString() )
                .employeeId( notExistingId.toString() )
                .description( "Test description" )
                .totalTime( Duration.ofHours( 8 ) )
                .build();

        String jsonContent = objectMapper.writeValueAsString( timeEntryCreateDTO );

        mockMvc.perform(
                post( "/api/timeentries" )
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
    @WithMockCustomUser
    @DisplayName("/POST createTimeEntry should return status code 400 when no time values")
    void createTimeEntry_ShouldReturnStatusCode400_WhenNoTimeValues() throws Exception {
        Project project = projectRepository.save( testProject );
        Employee employee = employeeRepository.save( testEmployee );
        assert project.getId() != null;
        assert employee.getId() != null;

        TimeEntryCreateDTO timeEntryCreateDTO = TimeEntryCreateDTO.builder()
                .projectId( project.getId().toString() )
                .employeeId( employee.getId().toString() )
                .description( "Test description" )
                .build();

        String jsonContent = objectMapper.writeValueAsString( timeEntryCreateDTO );

        mockMvc.perform(
                post( "/api/timeentries" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
                        .with( csrf() )
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.errors" ).isArray()
        );
    }

    @Test
    @WithMockCustomUser
    @DisplayName("/POST createTimeEntry should return status code 400 when start and end time invalid")
    void createTimeEntry_ShouldReturnStatusCode400_WhenStartAndEndTimeInvalid() throws Exception {
        Instant start = Instant.now();
        Project project = projectRepository.save( testProject );
        Employee employee = employeeRepository.save( testEmployee );
        assert project.getId() != null;
        assert employee.getId() != null;

        TimeEntryCreateDTO timeEntryCreateDTO = TimeEntryCreateDTO.builder()
                .projectId( project.getId().toString() )
                .employeeId( employee.getId().toString() )
                .description( "Test description" )
                .startTime( start.plusSeconds( 3600 ) )
                .endTime( start )
                .build();

        String jsonContent = objectMapper.writeValueAsString( timeEntryCreateDTO );

        mockMvc.perform(
                post( "/api/timeentries" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
                        .with( csrf() )
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.errors" ).isArray()
        );
    }

    @Test
    @WithMockCustomUser
    @DisplayName("/POST createTimeEntry should return status code 400 when pause time greater than total time")
    void createTimeEntry_ShouldReturnStatusCode400_WhenPauseTimeIsGreaterThanTotalTime() throws Exception {
        Project project = projectRepository.save( testProject );
        Employee employee = employeeRepository.save( testEmployee );
        assert project.getId() != null;
        assert employee.getId() != null;

        TimeEntryCreateDTO timeEntryCreateDTO = TimeEntryCreateDTO.builder()
                .projectId( project.getId().toString() )
                .employeeId( employee.getId().toString() )
                .description( "Test description" )
                .totalTime( Duration.ofSeconds( 3600 ) )
                .pauseTime( Duration.ofSeconds( 7200 ) )
                .build();

        String jsonContent = objectMapper.writeValueAsString( timeEntryCreateDTO );

        mockMvc.perform(
                post( "/api/timeentries" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
                        .with( csrf() )
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.errors" ).isArray()
        );
    }

    @Test
    @WithMockCustomUser
    @DisplayName("/POST createTimeEntry should return status code 201 when request body is valid")
    void createTimeEntry_ShouldReturnStatusCode201_WhenRequestBodyIsValid() throws Exception {
        Project project = projectRepository.save( testProject );
        Employee employee = employeeRepository.save( testEmployee );
        assert project.getId() != null;
        assert employee.getId() != null;

        TimeEntryCreateDTO timeEntryCreateDTO = TimeEntryCreateDTO.builder()
                .projectId( project.getId().toString() )
                .employeeId( employee.getId().toString() )
                .description( "Test description" )
                .totalTime( Duration.ofSeconds( 28800 ) )
                .pauseTime( Duration.ofSeconds( 3600 ) )
                .build();

        String jsonContent = objectMapper.writeValueAsString( timeEntryCreateDTO );

        mockMvc.perform(
                post( "/api/timeentries" )
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
        ).andExpect(
                jsonPath( "$.description" ).value( timeEntryCreateDTO.getDescription() )
        );
    }

    @Test
    @WithMockCustomUser
    @DisplayName("/DELETE deleteTimeEntryById should return status code 404 when time entry not found")
    void deleteTimeEntryById_ShouldReturnStatusCode404_WhenTimeEntryNotFound() throws Exception {
        mockMvc.perform(
                delete( "/api/timeentries/{id}", notExistingId.toString() )
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
    @WithMockCustomUser
    @DisplayName("/DELETE deleteTimeEntryById should return status code 204 when time entry was found")
    void deleteTimeEntryById_ShouldReturnStatusCode204_WhenTimeEntryWasFound() throws Exception {
        projectRepository.save( testProject );
        employeeRepository.save( testEmployee );
        TimeEntry timeEntry = timeEntryRepository.save( testTimeEntry );
        assert timeEntry.getId() != null;
        mockMvc.perform(
                delete( "/api/timeentries/{id}", timeEntry.getId().toString() )
                        .with( csrf() )
        ).andExpect(
                status().isNoContent()
        );
    }

}