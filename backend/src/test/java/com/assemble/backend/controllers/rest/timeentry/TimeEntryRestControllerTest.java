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
import com.assemble.backend.models.dtos.timeentry.TimeEntryUpdateDTO;
import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.models.entities.auth.UserRole;
import com.assemble.backend.models.entities.employee.Employee;
import com.assemble.backend.models.entities.project.*;
import com.assemble.backend.models.entities.timeentry.TimeEntry;
import com.assemble.backend.repositories.auth.UserRepository;
import com.assemble.backend.repositories.employee.EmployeeRepository;
import com.assemble.backend.repositories.project.ProjectAssignmentRepository;
import com.assemble.backend.repositories.project.ProjectRepository;
import com.assemble.backend.repositories.timeentry.TimeEntryRepository;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private TimeEntryRepository timeEntryRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectAssignmentRepository projectAssignmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static User testUser;
    private static Employee testEmployee;
    private static Employee otherTestEmployee;
    private static Project testProject;
    private static ProjectAssignment testProjectAssignment;
    private static TimeEntry testTimeEntry;
    private static final UUID notExistingId = UuidCreator.getTimeOrderedEpoch();

    @BeforeEach
    void setup() {
        testUser = userRepository.save(
                User.builder()
                        .firstname( "Test" )
                        .lastname( "User" )
                        .username( "testuser2" )
                        .email( "testuser2@example.com" )
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

        otherTestEmployee = employeeRepository.save(
                Employee.builder()
                        .firstname( "Max" )
                        .lastname( "Mustermann" )
                        .email( "testuser2@example.com" )
                        .user( null )
                        .build()
        );

        testProject = projectRepository.save(
                Project.builder()
                        .name( "Test Project" )
                        .description( "Test Project Description" )
                        .category( "Maintanance" )
                        .color( ProjectColor.PURPLE )
                        .stage( ProjectStage.PROPOSAL )
                        .type( ProjectType.EXTERNAL )
                        .build()
        );

        testProjectAssignment = projectAssignmentRepository.save(
                ProjectAssignment.builder()
                        .employee( testEmployee )
                        .project( testProject )
                        .hourlyRate( BigDecimal.valueOf( 50 ) )
                        .build()
        );

        testTimeEntry = TimeEntry.builder()
                .description( "Test Time Entry" )
                .project( testProject )
                .employee( testEmployee )
                .date( LocalDate.now() )
                .totalTime( Duration.ofHours( 8 ) )
                .pauseTime( Duration.ofHours( 2 ) )
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
    @WithMockCustomUser(saveToDatabase = true)
    @DisplayName("/GET getOwnTimeEntries should return status code 400 when aroundDate param is invalid")
    void getOwnTimeEntries_ShouldReturnStatusCode400_WhenAroundDateParamIsInvalid() throws Exception {
        User mockedUserFromDB = userRepository.findByUsername( "testuser" ).orElseThrow();
        testEmployee.setUser( mockedUserFromDB );
        employeeRepository.save( testEmployee );

        mockMvc.perform(
                get( "/api/timeentries/me" )
                        .param( "aroundDate", " " )
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.message" ).isNotEmpty()
        );
    }

    @Test
    @WithMockCustomUser(saveToDatabase = true)
    @DisplayName("/GET getOwnTimeEntries should return status code 400 when exactDate param is invalid")
    void getOwnTimeEntries_ShouldReturnStatusCode400_WhenExactDateParamIsInvalid() throws Exception {
        User mockedUserFromDB = userRepository.findByUsername( "testuser" ).orElseThrow();
        testEmployee.setUser( mockedUserFromDB );
        employeeRepository.save( testEmployee );

        mockMvc.perform(
                get( "/api/timeentries/me" )
                        .param( "exactDate", " " )
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.message" ).isNotEmpty()
        );
    }

    @Test
    @WithMockCustomUser(saveToDatabase = true)
    @DisplayName("/GET getOwnTimeEntries should return status code 200 and a list of TimeEntryDTO when called with exactDate")
    void getOwnTimeEntries_ShouldReturnStatusCode200AndAListOfTimeEntryDTO_WhenCalledWithExactDate() throws Exception {
        User mockedUserFromDB = userRepository.findByUsername( "testuser" ).orElseThrow();
        testEmployee.setUser( mockedUserFromDB );
        employeeRepository.save( testEmployee );

        TimeEntry timeEntry = timeEntryRepository.save( testTimeEntry );
        assert timeEntry.getId() != null;
        mockMvc.perform(
                get( "/api/timeentries/me" )
                        .param( "exactDate", LocalDate.now().format( DateTimeFormatter.ofPattern( "yyyy-MM-dd" ) ) )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$[0].id" ).value( timeEntry.getId().toString() )
        );
    }

    @Test
    @WithMockCustomUser(saveToDatabase = true)
    @DisplayName("/GET getOwnTimeEntries should return status code 200 and a list of TimeEntryDTO when called with aroundDate")
    void getOwnTimeEntries_ShouldReturnStatusCode200AndAListOfTimeEntryDTO_WhenCalledWithAroundDate() throws Exception {
        User mockedUserFromDB = userRepository.findByUsername( "testuser" ).orElseThrow();
        testEmployee.setUser( mockedUserFromDB );
        employeeRepository.save( testEmployee );

        TimeEntry timeEntry = timeEntryRepository.save( testTimeEntry );
        assert timeEntry.getId() != null;
        mockMvc.perform(
                get( "/api/timeentries/me" )
                        .param( "aroundDate", LocalDate.now().format( DateTimeFormatter.ofPattern( "yyyy-MM-dd" ) ) )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$[0].id" ).value( timeEntry.getId().toString() )
        );
    }

    @Test
    @WithMockCustomUser(saveToDatabase = true)
    @DisplayName("/GET getOwnTimeEntries should return status code 200 and an empty list")
    void getOwnTimeEntries_ShouldReturnStatusCode200AndAnEmptyList() throws Exception {
        User mockedUserFromDB = userRepository.findByUsername( "testuser" ).orElseThrow();
        testEmployee.setUser( mockedUserFromDB );
        employeeRepository.save( testEmployee );

        mockMvc.perform(
                get( "/api/timeentries/me" )
                        .param( "aroundDate", LocalDate.now().format( DateTimeFormatter.ofPattern( "yyyy-MM-dd" ) ) )
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
    @WithMockCustomUser(saveToDatabase = true)
    @DisplayName("/GET getOwnTimeEntries should return status code 404 when called with exactDate and no employee found")
    void getOwnTimeEntries_ShouldReturnStatusCode404_WhenCalledWithExactDateAndNoEmployeeFound() throws Exception {
        mockMvc.perform(
                get( "/api/timeentries/me" )
                        .param( "exactDate", LocalDate.now().format( DateTimeFormatter.ofPattern( "yyyy-MM-dd" ) ) )
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.message" ).isNotEmpty()
        );
    }

    @Test
    @WithMockCustomUser(saveToDatabase = true)
    @DisplayName("/GET getOwnTimeEntryById should return status code 404 when time entry not found")
    void getOwnTimeEntryById_ShouldReturnStatusCode404_WhenTimeEntryNotFound() throws Exception {
        User mockedUserFromDB = userRepository.findByUsername( "testuser" ).orElseThrow();
        testEmployee.setUser( mockedUserFromDB );
        employeeRepository.save( testEmployee );

        mockMvc.perform(
                get( "/api/timeentries/me/" + UuidCreator.getTimeOrderedEpoch().toString() )
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.message" ).isNotEmpty()
        );
    }

    @Test
    @WithMockCustomUser(saveToDatabase = true)
    @DisplayName("/GET getOwnTimeEntryById should return status code 404 when employee not found")
    void getOwnTimeEntryById_ShouldReturnStatusCode404_WhenEmployeeNotFound() throws Exception {
        TimeEntry entry = timeEntryRepository.save( testTimeEntry );
        assert entry.getId() != null;
        mockMvc.perform(
                get( "/api/timeentries/me/" + entry.getId().toString() )
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.message" ).isNotEmpty()
        );
    }

    @Test
    @WithMockCustomUser(saveToDatabase = true)
    @DisplayName("/GET getOwnTimeEntryById should return status code 400 when user does not own time entry")
    void getOwnTimeEntryById_ShouldReturnStatusCode400_WhenUserDoesNotOwnTimeEntry() throws Exception {
        User mockedUserFromDB = userRepository.findByUsername( "testuser" ).orElseThrow();
        otherTestEmployee.setUser( mockedUserFromDB );
        employeeRepository.save( otherTestEmployee );

        TimeEntry entry = timeEntryRepository.save( testTimeEntry );
        assert entry.getId() != null;
        mockMvc.perform(
                get( "/api/timeentries/me/" + entry.getId().toString() )
        ).andExpect(
                status().isForbidden()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.message" ).isNotEmpty()
        );
    }

    @Test
    @WithMockCustomUser(saveToDatabase = true)
    @DisplayName("/GET getOwnTimeEntryById should return status code 200 when called")
    void getOwnTimeEntryById_ShouldReturnStatusCode200_WhenCalled() throws Exception {
        User mockedUserFromDB = userRepository.findByUsername( "testuser" ).orElseThrow();
        testEmployee.setUser( mockedUserFromDB );
        employeeRepository.save( testEmployee );

        TimeEntry entry = timeEntryRepository.save( testTimeEntry );
        assert entry.getId() != null;
        mockMvc.perform(
                get( "/api/timeentries/me/" + entry.getId().toString() )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.id" ).value( entry.getId().toString() )
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
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/POST createTimeEntry should return status code 404 when project assignment not found")
    void createTimeEntry_ShouldReturnStatusCode404_WhenProjectAssignmentNotFound() throws Exception {
        TimeEntryCreateDTO timeEntryCreateDTO = TimeEntryCreateDTO.builder()
                .projectId( notExistingId.toString() )
                .employeeId( notExistingId.toString() )
                .description( "Test description" )
                .date( LocalDate.now() )
                .totalTime( Duration.ofHours( 9 ) )
                .pauseTime( Duration.ofHours( 1 ) )
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
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/POST createTimeEntry should return status code 400 when start time is after end time")
    void createTimeEntry_ShouldReturnStatusCode400_WhenStartTimeIsAfterEndTime() throws Exception {
        assert testProject.getId() != null;
        assert testEmployee.getId() != null;

        TimeEntryCreateDTO timeEntryCreateDTO = TimeEntryCreateDTO.builder()
                .projectId( testProject.getId().toString() )
                .employeeId( testEmployee.getId().toString() )
                .description( "Test description" )
                .date( LocalDate.now() )
                .startTime( Instant.now() )
                .endTime( Instant.now().minusSeconds( 3600 ) )
                .totalTime( Duration.ofHours( 9 ) )
                .pauseTime( Duration.ofHours( 1 ) )
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
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/POST createTimeEntry should return status code 400 when total time is less than pause time")
    void createTimeEntry_ShouldReturnStatusCode400_WhenTotalTimeIsLessThanPauseTime() throws Exception {
        assert testProject.getId() != null;
        assert testEmployee.getId() != null;

        TimeEntryCreateDTO timeEntryCreateDTO = TimeEntryCreateDTO.builder()
                .projectId( testProject.getId().toString() )
                .employeeId( testEmployee.getId().toString() )
                .description( "Test description" )
                .date( LocalDate.now() )
                .totalTime( Duration.ofHours( 1 ) )
                .pauseTime( Duration.ofHours( 2 ) )
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
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/POST createTimeEntry should return status code 400 when description invalid")
    void createTimeEntry_ShouldReturnStatusCode400_WhenDescriptionInvalid() throws Exception {
        assert testProject.getId() != null;
        assert testEmployee.getId() != null;

        TimeEntryCreateDTO timeEntryCreateDTO = TimeEntryCreateDTO.builder()
                .projectId( testProject.getId().toString() )
                .employeeId( testEmployee.getId().toString() )
                .description( "Test" )
                .date( LocalDate.now() )
                .totalTime( Duration.ofHours( 9 ) )
                .pauseTime( Duration.ofHours( 1 ) )
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
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/POST createTimeEntry should return status code 400 when project id and employee id invalid")
    void createTimeEntry_ShouldReturnStatusCode400_WhenProjectIdAndEmployeeIdInvalid() throws Exception {

        TimeEntryCreateDTO timeEntryCreateDTO = TimeEntryCreateDTO.builder()
                .projectId( " " )
                .employeeId( " " )
                .description( "Test Description" )
                .date( LocalDate.now() )
                .totalTime( Duration.ofHours( 9 ) )
                .pauseTime( Duration.ofHours( 1 ) )
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
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/POST createTimeEntry should return status code 400 when project assignment is inactive")
    void createTimeEntry_ShouldReturnStatusCode400_WhenProjectAssignmentIsInactive() throws Exception {
        testProjectAssignment.setActive( false );
        projectAssignmentRepository.save(
                testProjectAssignment
        );

        assert testProject.getId() != null;
        assert testEmployee.getId() != null;

        TimeEntryCreateDTO timeEntryCreateDTO = TimeEntryCreateDTO.builder()
                .projectId( testProject.getId().toString() )
                .employeeId( testEmployee.getId().toString() )
                .description( "Test description" )
                .date( LocalDate.now() )
                .totalTime( Duration.ofHours( 9 ) )
                .pauseTime( Duration.ofHours( 1 ) )
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
                jsonPath( "$.message" ).isNotEmpty()
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/POST createTimeEntry should return status code 201 when request body is valid")
    void createTimeEntry_ShouldReturnStatusCode201_WhenRequestBodyIsValid() throws Exception {
        assert testProject.getId() != null;
        assert testEmployee.getId() != null;

        TimeEntryCreateDTO timeEntryCreateDTO = TimeEntryCreateDTO.builder()
                .projectId( testProject.getId().toString() )
                .employeeId( testEmployee.getId().toString() )
                .description( "Test description" )
                .date( LocalDate.now() )
                .totalTime( Duration.ofHours( 9 ) )
                .pauseTime( Duration.ofHours( 1 ) )
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
    @WithMockCustomUser(saveToDatabase = true)
    @DisplayName("/POST createOwnTimeEntry should return status code 404 when related employee not found")
    void createOwnTimeEntry_ShouldReturnStatusCode404_WhenRelatedEmployeeNotFound() throws Exception {
        User mockedUserFromDB = userRepository.findByUsername( "testuser" ).orElseThrow();
        otherTestEmployee.setUser( mockedUserFromDB );
        employeeRepository.save( otherTestEmployee );

        assert testProject.getId() != null;
        assert testEmployee.getId() != null;

        TimeEntryCreateDTO timeEntryCreateDTO = TimeEntryCreateDTO.builder()
                .projectId( testProject.getId().toString() )
                .employeeId( testEmployee.getId().toString() )
                .description( "Test description" )
                .date( LocalDate.now() )
                .totalTime( Duration.ofHours( 9 ) )
                .pauseTime( Duration.ofHours( 1 ) )
                .build();

        String jsonContent = objectMapper.writeValueAsString( timeEntryCreateDTO );

        mockMvc.perform(
                post( "/api/timeentries/me" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
                        .with( csrf() )
        ).andExpect(
                status().isForbidden()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.message" ).isNotEmpty()
        );
    }

    @Test
    @WithMockCustomUser(saveToDatabase = true)
    @DisplayName("/POST createOwnTimeEntry should return status code 201 when called")
    void createOwnTimeEntry_ShouldReturnStatusCode201_WhenCalled() throws Exception {
        User mockedUserFromDB = userRepository.findByUsername( "testuser" ).orElseThrow();
        testEmployee.setUser( mockedUserFromDB );
        employeeRepository.save( testEmployee );

        assert testProject.getId() != null;
        assert testEmployee.getId() != null;

        TimeEntryCreateDTO timeEntryCreateDTO = TimeEntryCreateDTO.builder()
                .projectId( testProject.getId().toString() )
                .employeeId( testEmployee.getId().toString() )
                .description( "Test description" )
                .date( LocalDate.now() )
                .totalTime( Duration.ofHours( 9 ) )
                .pauseTime( Duration.ofHours( 1 ) )
                .build();

        String jsonContent = objectMapper.writeValueAsString( timeEntryCreateDTO );

        mockMvc.perform(
                post( "/api/timeentries/me" )
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
        ).andExpect(
                jsonPath( "$.employee.id" ).value( testEmployee.getId().toString() )
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/PATCH updateTimeEntry should return status code 404 when project assignment not found")
    void updateTimeEntry_ShouldReturnStatusCode404_WhenProjectAssignmentNotFound() throws Exception {
        TimeEntry timeEntry = timeEntryRepository.save( testTimeEntry );
        assert timeEntry.getId() != null;

        TimeEntryUpdateDTO dto = TimeEntryUpdateDTO
                .builder()
                .projectId( notExistingId.toString() )
                .employeeId( notExistingId.toString() )
                .build();

        String jsonContent = objectMapper.writeValueAsString( dto );

        mockMvc.perform(
                patch( "/api/timeentries/{id}", timeEntry.getId().toString() )
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
    @DisplayName("/PATCH updateTimeEntry should return status code 400 when start time is after end time")
    void updateTimeEntry_ShouldReturnStatusCode400_WhenStartTimeIsAfterEndTime() throws Exception {
        TimeEntry timeEntry = timeEntryRepository.save( testTimeEntry );
        assert timeEntry.getId() != null;
        assert testProject.getId() != null;
        assert testEmployee.getId() != null;

        TimeEntryUpdateDTO dto = TimeEntryUpdateDTO
                .builder()
                .projectId( testProject.getId().toString() )
                .employeeId( testEmployee.getId().toString() )
                .startTime( Instant.now() )
                .endTime( Instant.now().minusSeconds( 3600 ) )
                .build();

        String jsonContent = objectMapper.writeValueAsString( dto );

        mockMvc.perform(
                patch( "/api/timeentries/{id}", timeEntry.getId().toString() )
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
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/PATCH updateTimeEntry should return status code 400 when total time is less than pause time")
    void updateTimeEntry_ShouldReturnStatusCode400_WhenTotalTimeIsLessThanPauseTime() throws Exception {
        TimeEntry timeEntry = timeEntryRepository.save( testTimeEntry );
        assert timeEntry.getId() != null;
        assert testProject.getId() != null;
        assert testEmployee.getId() != null;

        TimeEntryUpdateDTO dto = TimeEntryUpdateDTO
                .builder()
                .projectId( testProject.getId().toString() )
                .employeeId( testEmployee.getId().toString() )
                .totalTime( Duration.ofHours( 1 ) )
                .pauseTime( Duration.ofHours( 2 ) )
                .build();

        String jsonContent = objectMapper.writeValueAsString( dto );

        mockMvc.perform(
                patch( "/api/timeentries/{id}", timeEntry.getId().toString() )
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
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/PATCH updateTimeEntry should return status code 400 when description is invalid")
    void updateTimeEntry_ShouldReturnStatusCode400_WhenDescriptionIsInvalid() throws Exception {
        TimeEntry timeEntry = timeEntryRepository.save( testTimeEntry );
        assert timeEntry.getId() != null;
        assert testProject.getId() != null;
        assert testEmployee.getId() != null;

        TimeEntryUpdateDTO dto = TimeEntryUpdateDTO
                .builder()
                .projectId( testProject.getId().toString() )
                .employeeId( testEmployee.getId().toString() )
                .description( "Test" )
                .build();

        String jsonContent = objectMapper.writeValueAsString( dto );

        mockMvc.perform(
                patch( "/api/timeentries/{id}", timeEntry.getId().toString() )
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
    @WithMockCustomUser(roles = { UserRole.MANAGER })
    @DisplayName("/PATCH updateTimeEntry should return status code 400 when assignment is inactive")
    void updateTimeEntry_ShouldReturnStatusCode400_WhenAssignmentIsInactive() throws Exception {
        TimeEntry timeEntry = timeEntryRepository.save( testTimeEntry );
        testProjectAssignment.setActive( false );
        projectAssignmentRepository.save( testProjectAssignment );

        assert timeEntry.getId() != null;
        assert testProject.getId() != null;
        assert testEmployee.getId() != null;

        TimeEntryUpdateDTO dto = TimeEntryUpdateDTO
                .builder()
                .projectId( testProject.getId().toString() )
                .employeeId( testEmployee.getId().toString() )
                .build();

        String jsonContent = objectMapper.writeValueAsString( dto );

        mockMvc.perform(
                patch( "/api/timeentries/{id}", timeEntry.getId().toString() )
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
    @DisplayName("/PATCH updateTimeEntry should return status code 200 when called")
    void updateTimeEntry_ShouldReturnStatusCode200_WhenCalled() throws Exception {
        TimeEntry timeEntry = timeEntryRepository.save( testTimeEntry );
        assert timeEntry.getId() != null;
        assert testProject.getId() != null;
        assert testEmployee.getId() != null;

        TimeEntryUpdateDTO dto = TimeEntryUpdateDTO
                .builder()
                .description( "Updated description" )
                .build();

        String jsonContent = objectMapper.writeValueAsString( dto );

        mockMvc.perform(
                patch( "/api/timeentries/{id}", timeEntry.getId().toString() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
                        .with( csrf() )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.description" ).value( dto.getDescription() )
        );
    }

    @Test
    @WithMockCustomUser()
    @DisplayName("/PATCH updateOwnTimeEntry should return status code 404 when related employee not found")
    void updateOwnTimeEntry_ShouldReturnStatusCode404_WhenRelatedEmployeeNotFound() throws Exception {
        TimeEntry timeEntry = timeEntryRepository.save( testTimeEntry );

        assert timeEntry.getId() != null;
        assert testProject.getId() != null;
        assert testEmployee.getId() != null;

        TimeEntryUpdateDTO dto = TimeEntryUpdateDTO
                .builder()
                .description( "Updated description" )
                .build();

        String jsonContent = objectMapper.writeValueAsString( dto );

        mockMvc.perform(
                patch( "/api/timeentries/me/{id}", timeEntry.getId().toString() )
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
    @WithMockCustomUser(saveToDatabase = true)
    @DisplayName("/PATCH updateOwnTimeEntry should return status code 404 when time entry not found")
    void updateOwnTimeEntry_ShouldReturnStatusCode404_WhenTimeEntryNotFound() throws Exception {
        User mockedUserFromDB = userRepository.findByUsername( "testuser" ).orElseThrow();
        testEmployee.setUser( mockedUserFromDB );
        employeeRepository.save( testEmployee );

        assert testProject.getId() != null;
        assert testEmployee.getId() != null;

        TimeEntryUpdateDTO dto = TimeEntryUpdateDTO
                .builder()
                .description( "Updated description" )
                .build();

        String jsonContent = objectMapper.writeValueAsString( dto );

        mockMvc.perform(
                patch( "/api/timeentries/me/{id}", UuidCreator.getTimeOrderedEpoch().toString() )
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
    @WithMockCustomUser(saveToDatabase = true)
    @DisplayName("/PATCH updateOwnTimeEntry should return status code 400 when user does not own time entry")
    void updateOwnTimeEntry_ShouldReturnStatusCode400_WhenUserDoesNotOwnTimeEntry() throws Exception {
        User mockedUserFromDB = userRepository.findByUsername( "testuser" ).orElseThrow();
        otherTestEmployee.setUser( mockedUserFromDB );
        employeeRepository.save( otherTestEmployee );

        TimeEntry timeEntry = timeEntryRepository.save( testTimeEntry );
        assert timeEntry.getId() != null;
        assert testProject.getId() != null;
        assert testEmployee.getId() != null;

        TimeEntryUpdateDTO dto = TimeEntryUpdateDTO
                .builder()
                .description( "Updated description" )
                .build();

        String jsonContent = objectMapper.writeValueAsString( dto );

        mockMvc.perform(
                patch( "/api/timeentries/me/{id}", timeEntry.getId().toString() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
                        .with( csrf() )
        ).andExpect(
                status().isForbidden()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.message" ).isNotEmpty()
        );
    }

    @Test
    @WithMockCustomUser(saveToDatabase = true)
    @DisplayName("/PATCH updateOwnTimeEntry should return status code 200 when called")
    void updateOwnTimeEntry_ShouldReturnStatusCode200_WhenCalled() throws Exception {
        User mockedUserFromDB = userRepository.findByUsername( "testuser" ).orElseThrow();
        testEmployee.setUser( mockedUserFromDB );
        employeeRepository.save( testEmployee );
        TimeEntry timeEntry = timeEntryRepository.save( testTimeEntry );
        assert timeEntry.getId() != null;
        assert testProject.getId() != null;
        assert testEmployee.getId() != null;

        TimeEntryUpdateDTO dto = TimeEntryUpdateDTO
                .builder()
                .description( "Updated description" )
                .build();

        String jsonContent = objectMapper.writeValueAsString( dto );

        mockMvc.perform(
                patch( "/api/timeentries/me/{id}", timeEntry.getId().toString() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( jsonContent )
                        .with( csrf() )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.description" ).value( dto.getDescription() )
        );
    }

    @Test
    @WithMockCustomUser(roles = { UserRole.MANAGER })
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
    @WithMockCustomUser(roles = { UserRole.MANAGER })
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

    @Test
    @WithMockCustomUser
    @DisplayName("/DELETE deleteOwnTimeEntryById should return status code 404 when related employee not found")
    void deleteOwnTimeEntryById_ShouldReturnStatusCode404_WhenRelatedEmployeeNotFound() throws Exception {
        mockMvc.perform(
                delete( "/api/timeentries/me/{id}", notExistingId.toString() )
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
    @WithMockCustomUser(saveToDatabase = true)
    @DisplayName("/DELETE deleteOwnTimeEntryById should return status code 404 when time entry not found")
    void deleteOwnTimeEntryById_ShouldReturnStatusCode404_WhenTimeEntryNotFound() throws Exception {
        User mockedUserFromDB = userRepository.findByUsername( "testuser" ).orElseThrow();
        testEmployee.setUser( mockedUserFromDB );
        employeeRepository.save( testEmployee );

        mockMvc.perform(
                delete( "/api/timeentries/me/{id}", notExistingId.toString() )
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
    @WithMockCustomUser(saveToDatabase = true)
    @DisplayName("/DELETE deleteOwnTimeEntryById should return status code 403 when user does not own time entry")
    void deleteOwnTimeEntryById_ShouldReturnStatusCode403_WhenUserDoesNotOwnTimeEntry() throws Exception {
        User mockedUserFromDB = userRepository.findByUsername( "testuser" ).orElseThrow();
        otherTestEmployee.setUser( mockedUserFromDB );
        employeeRepository.save( otherTestEmployee );
        TimeEntry timeEntry = timeEntryRepository.save( testTimeEntry );
        assert timeEntry.getId() != null;

        mockMvc.perform(
                delete( "/api/timeentries/me/{id}", timeEntry.getId().toString() )
                        .with( csrf() )
        ).andExpect(
                status().isForbidden()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.message" ).isNotEmpty()
        );
    }

    @Test
    @WithMockCustomUser(saveToDatabase = true)
    @DisplayName("/DELETE deleteOwnTimeEntryById should return status code 204 when called")
    void deleteOwnTimeEntryById_ShouldReturnStatusCode204_WhenCalled() throws Exception {
        User mockedUserFromDB = userRepository.findByUsername( "testuser" ).orElseThrow();
        testEmployee.setUser( mockedUserFromDB );
        employeeRepository.save( testEmployee );
        TimeEntry timeEntry = timeEntryRepository.save( testTimeEntry );
        assert timeEntry.getId() != null;

        mockMvc.perform(
                delete( "/api/timeentries/me/{id}", timeEntry.getId().toString() )
                        .with( csrf() )
        ).andExpect(
                status().isNoContent()
        );
    }
}