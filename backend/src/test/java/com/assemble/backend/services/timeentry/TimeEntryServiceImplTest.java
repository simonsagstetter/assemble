/*
 * assemble
 * TimeEntryServiceImplTest.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.timeentry;

import com.assemble.backend.models.dtos.auth.UserDTO;
import com.assemble.backend.models.dtos.employee.EmployeeDTO;
import com.assemble.backend.models.dtos.project.ProjectDTO;
import com.assemble.backend.models.dtos.timeentry.TimeEntryCreateDTO;
import com.assemble.backend.models.dtos.timeentry.TimeEntryDTO;
import com.assemble.backend.models.dtos.timeentry.TimeEntryUpdateDTO;
import com.assemble.backend.models.entities.auth.SecurityUser;
import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.models.entities.auth.UserAudit;
import com.assemble.backend.models.entities.auth.UserRole;
import com.assemble.backend.models.entities.employee.Employee;
import com.assemble.backend.models.entities.project.*;
import com.assemble.backend.models.entities.timeentry.TimeEntry;
import com.assemble.backend.models.mappers.timeentry.TimeEntryMapper;
import com.assemble.backend.repositories.employee.EmployeeRepository;
import com.assemble.backend.repositories.project.ProjectAssignmentRepository;
import com.assemble.backend.repositories.timeentry.TimeEntryRepository;
import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TimeEntryServiceImpl Unit Test")
class TimeEntryServiceImplTest {

    @Mock
    private TimeEntryRepository timeEntryRepository;

    @Mock
    private TimeEntryMapper timeEntryMapper;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private TimeEntryCalculationService calculationService;

    @Mock
    private ProjectAssignmentRepository projectAssignmentRepository;

    @InjectMocks
    private TimeEntryServiceImpl service;

    private static Project project;
    private static User user;
    private static User otherUser;
    private static Employee employee;
    private static Employee otherEmployee;
    private static TimeEntry timeEntry;
    private static TimeEntryDTO timeEntryDTO;
    private static ProjectAssignment projectAssignment;

    private static UUID recordId;
    private static UUID otherRecordId;
    private static UUID notExistingRecordId;

    private static final BigDecimal rate = BigDecimal.valueOf( 50 );
    private static final BigDecimal internalRate = BigDecimal.valueOf( 30 );

    @BeforeEach
    void init() {
        UserAudit userAudit = new UserAudit( null, "SYSTEM" );
        Instant now = Instant.now();

        recordId = UuidCreator.getTimeOrderedEpoch();
        otherRecordId = UuidCreator.getTimeOrderedEpoch();
        notExistingRecordId = UuidCreator.getTimeOrderedEpoch();

        project = Project.builder()
                .id( recordId )
                .no( "P000001" )
                .name( "Test Project" )
                .description( "Test Project Description" )
                .category( "Maintenance" )
                .stage( ProjectStage.PROPOSAL )
                .type( ProjectType.EXTERNAL )
                .color( ProjectColor.PURPLE )
                .createdDate( now )
                .lastModifiedDate( now )
                .createdBy( userAudit )
                .lastModifiedBy( userAudit )
                .build();

        ProjectDTO projectDTO = ProjectDTO.builder()
                .id( recordId.toString() )
                .no( project.getNo() )
                .name( project.getName() )
                .active( project.isActive() )
                .stage( project.getStage() )
                .type( project.getType() )
                .color( ProjectColor.PURPLE )
                .description( project.getDescription() )
                .category( project.getCategory() )
                .createdDate( project.getCreatedDate() )
                .lastModifiedDate( project.getLastModifiedDate() )
                .createdBy( project.getCreatedBy() )
                .lastModifiedBy( project.getLastModifiedBy() )
                .build();

        user = User.builder()
                .id( recordId )
                .username( "testuser" )
                .firstname( "Test" )
                .lastname( "User" )
                .email( "test@example.com" )
                .roles( List.of( UserRole.USER ) )
                .password( new BCryptPasswordEncoder().encode( "secret" ) )
                .createdDate( Instant.now() )
                .lastModifiedDate( Instant.now() )
                .createdBy( new UserAudit( null, "SYSTEM" ) )
                .lastModifiedBy( new UserAudit( null, "SYSTEM" ) )
                .build();

        otherUser = User.builder()
                .id( otherRecordId )
                .username( "testuser" )
                .firstname( "Test" )
                .lastname( "User" )
                .email( "test@example.com" )
                .roles( List.of( UserRole.USER ) )
                .password( new BCryptPasswordEncoder().encode( "secret" ) )
                .createdDate( Instant.now() )
                .lastModifiedDate( Instant.now() )
                .createdBy( new UserAudit( null, "SYSTEM" ) )
                .lastModifiedBy( new UserAudit( null, "SYSTEM" ) )
                .build();

        assert user.getId() != null;

        UserDTO userDto = UserDTO.builder()
                .id( user.getId().toString() )
                .firstname( user.getFirstname() )
                .lastname( user.getLastname() )
                .email( user.getEmail() )
                .username( user.getUsername() )
                .fullname( user.getFullname() )
                .roles( user.getRoles() )
                .build();

        otherEmployee = Employee.builder()
                .id( otherRecordId )
                .no( "E000001" )
                .firstname( "Max" )
                .lastname( "Mustermann" )
                .email( "max.mustermann@example.com" )
                .user( otherUser )
                .createdDate( now )
                .lastModifiedDate( now )
                .createdBy( userAudit )
                .lastModifiedBy( userAudit )
                .build();

        employee = Employee.builder()
                .id( recordId )
                .no( "E000001" )
                .firstname( "Max" )
                .lastname( "Mustermann" )
                .email( "max.mustermann@example.com" )
                .user( user )
                .createdDate( now )
                .lastModifiedDate( now )
                .createdBy( userAudit )
                .lastModifiedBy( userAudit )
                .build();

        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .id( recordId.toString() )
                .no( "E000001" )
                .fullname( employee.getFullname() )
                .firstname( employee.getFirstname() )
                .lastname( employee.getLastname() )
                .email( employee.getEmail() )
                .createdDate( employee.getCreatedDate() )
                .createdBy( userAudit )
                .lastModifiedBy( userAudit )
                .lastModifiedDate( employee.getLastModifiedDate() )
                .user( userDto )
                .build();

        projectAssignment = ProjectAssignment.builder()
                .id( recordId )
                .project( project )
                .employee( employee )
                .hourlyRate( rate )
                .active( true )
                .build();

        timeEntry = TimeEntry.builder()
                .id( recordId )
                .no( "T000001" )
                .project( project )
                .employee( employee )
                .description( "Test Time Entry Description" )
                .date( LocalDate.now() )
                .totalTime( Duration.ofHours( 9 ) )
                .pauseTime( Duration.ofHours( 1 ) )
                .rate( rate )
                .total( BigDecimal.valueOf( 8 * rate.longValue() ) )
                .totalInternal( BigDecimal.valueOf( 8 * internalRate.longValue() ) )
                .createdDate( now )
                .lastModifiedDate( now )
                .createdBy( userAudit )
                .lastModifiedBy( userAudit )
                .build();

        timeEntryDTO = TimeEntryDTO.builder()
                .id( recordId.toString() )
                .no( timeEntry.getNo() )
                .project( projectDTO )
                .employee( employeeDTO )
                .description( timeEntry.getDescription() )
                .date( timeEntry.getDate() )
                .totalTime( timeEntry.getTotalTime() )
                .pauseTime( timeEntry.getPauseTime() )
                .rate( timeEntry.getRate() )
                .total( timeEntry.getTotal() )
                .totalInternal( timeEntry.getTotalInternal() )
                .createdDate( timeEntry.getCreatedDate() )
                .createdBy( timeEntry.getCreatedBy() )
                .lastModifiedBy( timeEntry.getLastModifiedBy() )
                .lastModifiedDate( timeEntry.getLastModifiedDate() )
                .build();

        reset( timeEntryRepository, timeEntryMapper );
    }

    @Test
    @DisplayName("getAllTimeEntries should return empty list when no time entry exists in db")
    void getAllTimeEntries_ShouldReturnEmptyList_WhenNoTimeEntryExistsInDB() {
        when( timeEntryRepository.findAll() ).thenReturn( List.of() );

        List<TimeEntryDTO> actual = service.getAllTimeEntries();

        assertEquals( 0, actual.size() );

        verify( timeEntryRepository, times( 1 ) ).findAll();
    }

    @Test
    @DisplayName("getAllTimeEntries should return time entry list when time entry exists in db")
    void getAllTimeEntries_ShouldReturnTimeEntryList_WhenTimeEntryExistsInDB() {
        when( timeEntryRepository.findAll() ).thenReturn( List.of( timeEntry ) );
        when( timeEntryMapper.toTimeEntryDTO( timeEntry ) ).thenReturn( timeEntryDTO );

        List<TimeEntryDTO> actual = service.getAllTimeEntries();

        assertEquals( 1, actual.size() );
        assertEquals( timeEntryDTO, actual.getFirst() );

        verify( timeEntryRepository, times( 1 ) ).findAll();
        verify( timeEntryMapper, times( 1 ) ).toTimeEntryDTO( timeEntry );
    }

    @Test
    @DisplayName("getTimeEntriesByEmployeeId should return empty list when no time entry exists in db")
    void getTimeEntriesByEmployeeId_ShouldReturnEmptyList_WhenNoTimeEntryExistsInDB() {
        when( timeEntryRepository.findAllByEmployeeId( notExistingRecordId ) ).thenReturn( List.of() );

        List<TimeEntryDTO> actual = service.getTimeEntriesByEmployeeId( notExistingRecordId.toString() );

        assertEquals( 0, actual.size() );

        verify( timeEntryRepository, times( 1 ) ).findAllByEmployeeId( notExistingRecordId );
    }

    @Test
    @DisplayName("getTimeEntriesByEmployeeId should return time entry list when time entry exists in db")
    void getTimeEntriesByEmployeeId_ShouldReturnTimeEntryList_WhenTimeEntryExistsInDB() {
        when( timeEntryRepository.findAllByEmployeeId( recordId ) ).thenReturn( List.of( timeEntry ) );
        when( timeEntryMapper.toTimeEntryDTO( timeEntry ) ).thenReturn( timeEntryDTO );

        List<TimeEntryDTO> actual = service.getTimeEntriesByEmployeeId( recordId.toString() );

        assertEquals( 1, actual.size() );
        assertEquals( timeEntryDTO, actual.getFirst() );

        verify( timeEntryRepository, times( 1 ) ).findAllByEmployeeId( recordId );
        verify( timeEntryMapper, times( 1 ) ).toTimeEntryDTO( timeEntry );
    }

    @Test
    @DisplayName("getTimeEntriesByProjectId should return empty list when no time entry exists in db")
    void getTimeEntriesByProjectId_ShouldReturnEmptyList_WhenNoTimeEntryExistsInDB() {
        when( timeEntryRepository.findAllByProjectId( notExistingRecordId ) ).thenReturn( List.of() );

        List<TimeEntryDTO> actual = service.getTimeEntriesByProjectId( notExistingRecordId.toString() );

        assertEquals( 0, actual.size() );

        verify( timeEntryRepository, times( 1 ) ).findAllByProjectId( notExistingRecordId );
    }

    @Test
    @DisplayName("getTimeEntriesByProjectId should return time entry list when time entry exists in db")
    void getTimeEntriesByProjectId_ShouldReturnTimeEntryList_WhenTimeEntryExistsInDB() {
        when( timeEntryRepository.findAllByProjectId( recordId ) ).thenReturn( List.of( timeEntry ) );
        when( timeEntryMapper.toTimeEntryDTO( timeEntry ) ).thenReturn( timeEntryDTO );

        List<TimeEntryDTO> actual = service.getTimeEntriesByProjectId( recordId.toString() );

        assertEquals( 1, actual.size() );
        assertEquals( timeEntryDTO, actual.getFirst() );

        verify( timeEntryRepository, times( 1 ) ).findAllByProjectId( recordId );
        verify( timeEntryMapper, times( 1 ) ).toTimeEntryDTO( timeEntry );
    }

    @Test
    @DisplayName("getOwnTimeEntries should throw if aroundDate param is invalid date format")
    void getOwnTimeEntries_ShouldThrow_WhenAroundDateParamIsInvalidDateFormat() {
        when( employeeRepository.findByUser_Id( recordId ) ).thenReturn( Optional.of( employee ) );

        assertThrows( InvalidParameterException.class, () -> service.getOwnTimeEntries(
                new SecurityUser( user ), "invalidDateString"
        ) );

        verify( employeeRepository, times( 1 ) ).findByUser_Id( recordId );
    }

    @Test
    @DisplayName("getOwnTimeEntries should return empty list if user has no employee assigned")
    void getOwnTimeEntries_ShouldReturnEmptyList_WhenUserHasNoEmployeeAssigned() {
        when( employeeRepository.findByUser_Id( notExistingRecordId ) ).thenReturn( Optional.empty() );
        user.setId( notExistingRecordId );

        List<TimeEntryDTO> actual = assertDoesNotThrow( () -> service.getOwnTimeEntries(
                new SecurityUser( user ), "invalidDateString"
        ) );

        assertEquals( 0, actual.size() );

        verify( employeeRepository, times( 1 ) ).findByUser_Id( notExistingRecordId );
    }

    @Test
    @DisplayName("getOwnTimeEntries should return list of time entries when user has employee assigned")
    void getOwnTimeEntries_ShouldReturnListOfTimeEntries_WhenUserHasEmployeeAssigned() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );
        when( employeeRepository.findByUser_Id( recordId ) ).thenReturn( Optional.of( employee ) );
        when( timeEntryRepository.findAllByEmployee_IdAndDateIsBetween( eq( recordId ), any( LocalDate.class ), any( LocalDate.class ) ) )
                .thenReturn( List.of( timeEntry ) );
        when( timeEntryMapper.toTimeEntryDTO( timeEntry ) ).thenReturn( timeEntryDTO );

        List<TimeEntryDTO> actual = assertDoesNotThrow( () -> service.getOwnTimeEntries(
                new SecurityUser( user ), LocalDate.now().format( formatter )
        ) );

        assertEquals( 1, actual.size() );
        assertEquals( timeEntryDTO, actual.getFirst() );

        verify( employeeRepository, times( 1 ) ).findByUser_Id( recordId );
        verify( timeEntryRepository, times( 1 ) )
                .findAllByEmployee_IdAndDateIsBetween( eq( recordId ), any( LocalDate.class ), any( LocalDate.class ) );
        verify( timeEntryMapper, times( 1 ) ).toTimeEntryDTO( timeEntry );
    }

    @Test
    @DisplayName("getOwnTimeEntriesByDate should throw if aroundDate param is invalid date format")
    void getOwnTimeEntriesByDate_ShouldThrow_WhenAroundDateParamIsInvalidDateFormat() {
        when( employeeRepository.findByUser_Id( recordId ) ).thenReturn( Optional.of( employee ) );

        assertThrows( InvalidParameterException.class, () -> service.getOwnTimeEntriesByDate(
                new SecurityUser( user ), "invalidDateString"
        ) );

        verify( employeeRepository, times( 1 ) ).findByUser_Id( recordId );
    }

    @Test
    @DisplayName("getOwnTimeEntriesByDate should throw when user has no employee assigned")
    void getOwnTimeEntriesByDate_ShouldThrow_WhenUserHasNoEmployeeAssigned() {
        when( employeeRepository.findByUser_Id( notExistingRecordId ) ).thenReturn( Optional.empty() );
        user.setId( notExistingRecordId );

        assertThrows( EntityNotFoundException.class, () -> service.getOwnTimeEntriesByDate(
                new SecurityUser( user ), "invalidDateString"
        ) );


        verify( employeeRepository, times( 1 ) ).findByUser_Id( notExistingRecordId );
    }

    @Test
    @DisplayName("getOwnTimeEntriesByDate should return list of time entries when user has employee assigned")
    void getOwnTimeEntriesByDate_ShouldReturnListOfTimeEntries_WhenUserHasEmployeeAssigned() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );
        when( employeeRepository.findByUser_Id( recordId ) ).thenReturn( Optional.of( employee ) );

        when( timeEntryRepository.findAllByEmployee_IdAndDateIs( eq( recordId ), any( LocalDate.class ) ) )
                .thenReturn( List.of( timeEntry ) );
        when( timeEntryMapper.toTimeEntryDTO( timeEntry ) ).thenReturn( timeEntryDTO );

        List<TimeEntryDTO> actual = assertDoesNotThrow( () -> service.getOwnTimeEntriesByDate(
                new SecurityUser( user ), LocalDate.now().format( formatter )
        ) );

        assertEquals( 1, actual.size() );
        assertEquals( timeEntryDTO, actual.getFirst() );

        verify( employeeRepository, times( 1 ) ).findByUser_Id( recordId );
        verify( timeEntryRepository, times( 1 ) )
                .findAllByEmployee_IdAndDateIs( eq( recordId ), any( LocalDate.class ) );
        verify( timeEntryMapper, times( 1 ) ).toTimeEntryDTO( timeEntry );
    }

    @Test
    @DisplayName("getTimeEntryById should throw if no time entry exists in db")
    void getTimeEntryById_ShouldThrow_WhenNoTimeEntryExistsInDB() {
        when( timeEntryRepository.findById( notExistingRecordId ) ).thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class, () -> service.getTimeEntryById( notExistingRecordId.toString() ) );

        verify( timeEntryRepository, times( 1 ) ).findById( notExistingRecordId );
    }

    @Test
    @DisplayName("getTimeEntryById should return time entry dto instance when time entry exists in db")
    void getTimeEntryById_ShouldReturnTimeEntryDTOInstance_WhenTimeEntryExistsInDB() {
        when( timeEntryRepository.findById( recordId ) ).thenReturn( Optional.of( timeEntry ) );
        when( timeEntryMapper.toTimeEntryDTO( timeEntry ) ).thenReturn( timeEntryDTO );

        TimeEntryDTO actual = assertDoesNotThrow( () -> service.getTimeEntryById( recordId.toString() ) );

        assertEquals( timeEntryDTO, actual );

        verify( timeEntryRepository, times( 1 ) ).findById( recordId );
        verify( timeEntryMapper, times( 1 ) ).toTimeEntryDTO( timeEntry );
    }

    @Test
    @DisplayName("getOwnTimeEntryById should return time entry dto")
    void getOwnTimeEntryById_ShouldReturnTimeEntryDTO_WhenCalled() {
        when( timeEntryRepository.findById( recordId ) ).thenReturn( Optional.of( timeEntry ) );
        when( employeeRepository.findByUser_Id( recordId ) ).thenReturn( Optional.of( employee ) );
        when( timeEntryMapper.toTimeEntryDTO( timeEntry ) ).thenReturn( timeEntryDTO );

        TimeEntryDTO actual = assertDoesNotThrow( () -> service.getOwnTimeEntryById(
                recordId.toString(), new SecurityUser( user )
        ) );

        assertEquals( timeEntryDTO, actual );

        verify( timeEntryRepository, times( 1 ) ).findById( recordId );
        verify( employeeRepository, times( 1 ) ).findByUser_Id( recordId );
        verify( timeEntryMapper, times( 1 ) ).toTimeEntryDTO( timeEntry );
    }

    @Test
    @DisplayName("getOwnTimeEntryById should throw when user does not own timeentry")
    void getOwnTimeEntryById_ShouldThrow_WhenUserDoesNotOwnTimeEntry() {
        when( timeEntryRepository.findById( recordId ) ).thenReturn( Optional.of( timeEntry ) );

        when( employeeRepository.findByUser_Id( otherRecordId ) ).thenReturn( Optional.of( otherEmployee ) );

        assertThrows( AccessDeniedException.class, () -> service.getOwnTimeEntryById(
                recordId.toString(), new SecurityUser( otherUser )
        ) );

        verify( timeEntryRepository, times( 1 ) ).findById( recordId );
        verify( employeeRepository, times( 1 ) ).findByUser_Id( otherRecordId );
    }

    @Test
    @DisplayName("getOwnTimeEntryById should throw when employee cant be found")
    void getOwnTimeEntryById_ShouldThrow_WhenEmployeeCantBeFound() {
        when( timeEntryRepository.findById( recordId ) ).thenReturn( Optional.of( timeEntry ) );

        when( employeeRepository.findByUser_Id( notExistingRecordId ) ).thenReturn( Optional.empty() );

        otherUser.setId( notExistingRecordId );

        assertThrows( EntityNotFoundException.class, () -> service.getOwnTimeEntryById(
                recordId.toString(), new SecurityUser( otherUser )
        ) );

        verify( timeEntryRepository, times( 1 ) ).findById( recordId );
        verify( employeeRepository, times( 1 ) ).findByUser_Id( notExistingRecordId );
    }

    @Test
    @DisplayName("getOwnTimeEntryById should throw when time entry cant be found")
    void getOwnTimeEntryById_ShouldThrow_WhenTimeEntryCantBeFound() {
        when( timeEntryRepository.findById( notExistingRecordId ) ).thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class, () -> service.getOwnTimeEntryById(
                notExistingRecordId.toString(), new SecurityUser( otherUser )
        ) );

        verify( timeEntryRepository, times( 1 ) ).findById( notExistingRecordId );
    }

    @Test
    @DisplayName("createTimeEntry should throw when projectassignment does not exist in db")
    void createTimeEntry_ShouldThrow_WhenProjectDoesNotExistInDB() {
        TimeEntryCreateDTO dto = TimeEntryCreateDTO.builder()
                .projectId( notExistingRecordId.toString() )
                .employeeId( notExistingRecordId.toString() )
                .description( "Test description" )
                .date( LocalDate.now() )
                .pauseTime( Duration.ofHours( 1 ) )
                .totalTime( Duration.ofHours( 8 ) )
                .build();

        when( projectAssignmentRepository.findByProject_IdAndEmployee_Id( notExistingRecordId, notExistingRecordId ) )
                .thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class, () ->
                service.createTimeEntry( dto )
        );

        verify( projectAssignmentRepository, times( 1 ) )
                .findByProject_IdAndEmployee_Id( notExistingRecordId, notExistingRecordId );
    }

    @Test
    @DisplayName("createTimeEntry should throw when projectassignment is inactive")
    void createTimeEntry_ShouldThrow_WhenProjectAssignmentIsInactive() {
        TimeEntryCreateDTO dto = TimeEntryCreateDTO.builder()
                .projectId( recordId.toString() )
                .employeeId( recordId.toString() )
                .description( "Test description" )
                .date( LocalDate.now() )
                .pauseTime( Duration.ofHours( 1 ) )
                .totalTime( Duration.ofHours( 8 ) )
                .build();

        projectAssignment.setActive( false );

        when( projectAssignmentRepository.findByProject_IdAndEmployee_Id( recordId, recordId ) )
                .thenReturn( Optional.of( projectAssignment ) );

        assertThrows( InvalidParameterException.class, () ->
                service.createTimeEntry( dto )
        );

        verify( projectAssignmentRepository, times( 1 ) )
                .findByProject_IdAndEmployee_Id( recordId, recordId );
    }

    @Test
    @DisplayName("createTimeEntry should return instance of time entry dto when called with durations")
    void createTimeEntry_ShouldReturnTimeEntryDTOInstance_WhenCalledWithDurations() {
        TimeEntryCreateDTO dto = TimeEntryCreateDTO.builder()
                .projectId( recordId.toString() )
                .employeeId( recordId.toString() )
                .description( "Test description" )
                .date( LocalDate.now() )
                .totalTime( Duration.ofHours( 9 ) )
                .pauseTime( Duration.ofHours( 1 ) )
                .build();

        when( projectAssignmentRepository.findByProject_IdAndEmployee_Id( recordId, recordId ) )
                .thenReturn( Optional.of( projectAssignment ) );

        when( timeEntryMapper.toTimeEntry( dto, employee, project ) )
                .thenReturn( timeEntry );

        when( calculationService.calculateTotal( dto.getTotalTime(), dto.getPauseTime(), projectAssignment.getHourlyRate() ) )
                .thenReturn( BigDecimal.valueOf( 8 * projectAssignment.getHourlyRate().longValue() ) );

        when( calculationService.calculateTotal( dto.getTotalTime(), dto.getPauseTime(), internalRate ) )
                .thenReturn( BigDecimal.valueOf( 8 * 30 ) );

        when( timeEntryRepository.save( timeEntry ) ).thenReturn( timeEntry );

        when( timeEntryMapper.toTimeEntryDTO( timeEntry ) ).thenReturn( timeEntryDTO );

        TimeEntryDTO actual = assertDoesNotThrow( () -> service.createTimeEntry( dto ) );
        assertEquals( timeEntryDTO, actual );

        verify( projectAssignmentRepository, times( 1 ) ).findByProject_IdAndEmployee_Id( recordId, recordId );
        verify( timeEntryMapper, times( 1 ) ).toTimeEntry( dto, employee, project );
        verify( calculationService, times( 1 ) ).calculateTotal( dto.getTotalTime(), dto.getPauseTime(), projectAssignment.getHourlyRate() );
        verify( calculationService, times( 1 ) ).calculateTotal( dto.getTotalTime(), dto.getPauseTime(), internalRate );
        verify( timeEntryRepository, times( 1 ) ).save( timeEntry );
        verify( timeEntryMapper, times( 1 ) ).toTimeEntryDTO( timeEntry );
    }

    @Test
    @DisplayName("createTimeEntry should return instance of time entry dto when called with time range")
    void createTimeEntry_ShouldReturnTimeEntryDTOInstance_WhenCalledWithTimeRange() {
        TimeEntryCreateDTO dto = TimeEntryCreateDTO.builder()
                .projectId( recordId.toString() )
                .employeeId( recordId.toString() )
                .description( "Test description" )
                .date( LocalDate.now() )
                .startTime( Instant.now() )
                .endTime( Instant.now().plus( 9, ChronoUnit.HOURS ) )
                .totalTime( Duration.ofHours( 9 ) )
                .pauseTime( Duration.ofHours( 1 ) )
                .build();

        when( projectAssignmentRepository.findByProject_IdAndEmployee_Id( recordId, recordId ) )
                .thenReturn( Optional.of( projectAssignment ) );

        when( timeEntryMapper.toTimeEntry( dto, employee, project ) )
                .thenReturn( timeEntry );

        when( calculationService.calculateTotal( dto.getTotalTime(), dto.getPauseTime(), projectAssignment.getHourlyRate() ) )
                .thenReturn( BigDecimal.valueOf( 8 * projectAssignment.getHourlyRate().longValue() ) );

        when( calculationService.calculateTotal( dto.getTotalTime(), dto.getPauseTime(), internalRate ) )
                .thenReturn( BigDecimal.valueOf( 8 * 30 ) );

        when( timeEntryRepository.save( timeEntry ) ).thenReturn( timeEntry );

        when( timeEntryMapper.toTimeEntryDTO( timeEntry ) ).thenReturn( timeEntryDTO );

        TimeEntryDTO actual = assertDoesNotThrow( () -> service.createTimeEntry( dto ) );
        assertEquals( timeEntryDTO, actual );

        verify( projectAssignmentRepository, times( 1 ) ).findByProject_IdAndEmployee_Id( recordId, recordId );
        verify( timeEntryMapper, times( 1 ) ).toTimeEntry( dto, employee, project );
        verify( calculationService, times( 1 ) ).calculateTotal( dto.getTotalTime(), dto.getPauseTime(), projectAssignment.getHourlyRate() );
        verify( calculationService, times( 1 ) ).calculateTotal( dto.getTotalTime(), dto.getPauseTime(), internalRate );
        verify( timeEntryRepository, times( 1 ) ).save( timeEntry );
        verify( timeEntryMapper, times( 1 ) ).toTimeEntryDTO( timeEntry );
    }

    @Test
    @DisplayName("createTimeEntry should return instance of time entry dto when called with zero hourly rate")
    void createTimeEntry_ShouldReturnTimeEntryDTOInstance_WhenCalledWithZeroHourlyRate() {
        TimeEntryCreateDTO dto = TimeEntryCreateDTO.builder()
                .projectId( recordId.toString() )
                .employeeId( recordId.toString() )
                .description( "Test description" )
                .date( LocalDate.now() )
                .totalTime( Duration.ofHours( 9 ) )
                .pauseTime( Duration.ofHours( 1 ) )
                .build();

        projectAssignment.setHourlyRate( BigDecimal.ZERO );

        when( projectAssignmentRepository.findByProject_IdAndEmployee_Id( recordId, recordId ) )
                .thenReturn( Optional.of( projectAssignment ) );

        when( timeEntryMapper.toTimeEntry( dto, employee, project ) )
                .thenReturn( timeEntry );

        when( calculationService.calculateTotal( dto.getTotalTime(), dto.getPauseTime(), rate ) )
                .thenReturn( BigDecimal.valueOf( 8 * rate.longValue() ) );

        when( calculationService.calculateTotal( dto.getTotalTime(), dto.getPauseTime(), internalRate ) )
                .thenReturn( BigDecimal.valueOf( 8 * 30 ) );

        when( timeEntryRepository.save( timeEntry ) ).thenReturn( timeEntry );

        when( timeEntryMapper.toTimeEntryDTO( timeEntry ) ).thenReturn( timeEntryDTO );

        TimeEntryDTO actual = assertDoesNotThrow( () -> service.createTimeEntry( dto ) );
        assertEquals( timeEntryDTO, actual );

        verify( projectAssignmentRepository, times( 1 ) ).findByProject_IdAndEmployee_Id( recordId, recordId );
        verify( timeEntryMapper, times( 1 ) ).toTimeEntry( dto, employee, project );
        verify( calculationService, times( 1 ) ).calculateTotal( dto.getTotalTime(), dto.getPauseTime(), rate );
        verify( calculationService, times( 1 ) ).calculateTotal( dto.getTotalTime(), dto.getPauseTime(), internalRate );
        verify( timeEntryRepository, times( 1 ) ).save( timeEntry );
        verify( timeEntryMapper, times( 1 ) ).toTimeEntryDTO( timeEntry );
    }

    @Test
    @DisplayName("createOwnTimeEntry should throw if related employee was not found")
    void createOwnTimeEntry_ShouldThrow_WhenRelatedEmployeeWasNotFound() {
        TimeEntryCreateDTO dto = TimeEntryCreateDTO.builder()
                .projectId( recordId.toString() )
                .employeeId( recordId.toString() )
                .description( "Test description" )
                .date( LocalDate.now() )
                .totalTime( Duration.ofHours( 9 ) )
                .pauseTime( Duration.ofHours( 1 ) )
                .build();

        when( employeeRepository.findByUser_Id( otherRecordId ) ).thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class, () -> service
                .createOwnTimeEntry( dto, new SecurityUser( otherUser ) )
        );

        verify( employeeRepository, times( 1 ) ).findByUser_Id( otherRecordId );
    }

    @Test
    @DisplayName("createOwnTimeEntry should throw if user does not own timeentry")
    void createOwnTimeEntry_ShouldThrow_WhenUserDoesNotOwnTimeEntry() {
        TimeEntryCreateDTO dto = TimeEntryCreateDTO.builder()
                .projectId( recordId.toString() )
                .employeeId( recordId.toString() )
                .description( "Test description" )
                .date( LocalDate.now() )
                .totalTime( Duration.ofHours( 9 ) )
                .pauseTime( Duration.ofHours( 1 ) )
                .build();

        when( employeeRepository.findByUser_Id( otherRecordId ) ).thenReturn( Optional.of( otherEmployee ) );

        assertThrows( AccessDeniedException.class, () -> service
                .createOwnTimeEntry( dto, new SecurityUser( otherUser ) )
        );

        verify( employeeRepository, times( 1 ) ).findByUser_Id( otherRecordId );
    }

    @Test
    @DisplayName("createOwnTimeEntry should return instance of time entry dto")
    void createOwnTimeEntry_ShouldReturnTimeEntryDTOInstance_WhenCalled() {
        TimeEntryCreateDTO dto = TimeEntryCreateDTO.builder()
                .projectId( recordId.toString() )
                .employeeId( recordId.toString() )
                .description( "Test description" )
                .date( LocalDate.now() )
                .totalTime( Duration.ofHours( 9 ) )
                .pauseTime( Duration.ofHours( 1 ) )
                .build();

        when( employeeRepository.findByUser_Id( recordId ) ).thenReturn( Optional.of( employee ) );

        when( projectAssignmentRepository.findByProject_IdAndEmployee_Id( recordId, recordId ) )
                .thenReturn( Optional.of( projectAssignment ) );

        when( timeEntryMapper.toTimeEntry( dto, employee, project ) )
                .thenReturn( timeEntry );

        when( calculationService.calculateTotal( dto.getTotalTime(), dto.getPauseTime(), projectAssignment.getHourlyRate() ) )
                .thenReturn( BigDecimal.valueOf( 8 * projectAssignment.getHourlyRate().longValue() ) );

        when( calculationService.calculateTotal( dto.getTotalTime(), dto.getPauseTime(), internalRate ) )
                .thenReturn( BigDecimal.valueOf( 8 * 30 ) );

        when( timeEntryRepository.save( timeEntry ) ).thenReturn( timeEntry );

        when( timeEntryMapper.toTimeEntryDTO( timeEntry ) ).thenReturn( timeEntryDTO );

        TimeEntryDTO actual = assertDoesNotThrow( () -> service.createOwnTimeEntry( dto, new SecurityUser( user ) ) );
        assertEquals( timeEntryDTO, actual );

        verify( projectAssignmentRepository, times( 1 ) ).findByProject_IdAndEmployee_Id( recordId, recordId );
        verify( timeEntryMapper, times( 1 ) ).toTimeEntry( dto, employee, project );
        verify( calculationService, times( 1 ) ).calculateTotal( dto.getTotalTime(), dto.getPauseTime(), projectAssignment.getHourlyRate() );
        verify( calculationService, times( 1 ) ).calculateTotal( dto.getTotalTime(), dto.getPauseTime(), internalRate );
        verify( timeEntryRepository, times( 1 ) ).save( timeEntry );
        verify( timeEntryMapper, times( 1 ) ).toTimeEntryDTO( timeEntry );
    }

    @Test
    @DisplayName("updateTimeEntry should throw if time entry not found")
    void updateTimeEntry_ShouldThrow_WhenTimeEntryNotFound() {
        assert employee.getId() != null;
        assert project.getId() != null;
        TimeEntryUpdateDTO dto = TimeEntryUpdateDTO.builder()
                .employeeId( employee.getId().toString() )
                .projectId( project.getId().toString() )
                .date( timeEntry.getDate() )
                .description( timeEntry.getDescription() )
                .totalTime( timeEntry.getTotalTime() )
                .pauseTime( timeEntry.getPauseTime() )
                .build();

        when( timeEntryRepository.findById( notExistingRecordId ) ).thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class, () -> service.updateTimeEntry(
                notExistingRecordId.toString(),
                dto
        ) );

        verify( timeEntryRepository, times( 1 ) ).findById( notExistingRecordId );
    }

    @Test
    @DisplayName("updateTimeEntry should throw if project assignment not found")
    void updateTimeEntry_ShouldThrow_WhenProjectAssignmentNotFound() {
        assert employee.getId() != null;
        assert project.getId() != null;
        TimeEntryUpdateDTO dto = TimeEntryUpdateDTO.builder()
                .employeeId( notExistingRecordId.toString() )
                .projectId( notExistingRecordId.toString() )
                .date( timeEntry.getDate() )
                .description( timeEntry.getDescription() )
                .totalTime( timeEntry.getTotalTime() )
                .pauseTime( timeEntry.getPauseTime() )
                .build();

        when( timeEntryRepository.findById( recordId ) ).thenReturn( Optional.of( timeEntry ) );
        when( projectAssignmentRepository.findByProject_IdAndEmployee_Id( notExistingRecordId, notExistingRecordId ) )
                .thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class, () -> service.updateTimeEntry(
                recordId.toString(),
                dto
        ) );

        verify( timeEntryRepository, times( 1 ) ).findById( recordId );
        verify( projectAssignmentRepository, times( 1 ) )
                .findByProject_IdAndEmployee_Id( notExistingRecordId, notExistingRecordId );
    }

    @Test
    @DisplayName("updateTimeEntry should return time entry dto when project assignment changed")
    void updateTimeEntry_ShouldReturnTimeEntryDTOInstance_WhenProjectAssignmentChanged() {
        assert employee.getId() != null;
        assert project.getId() != null;
        TimeEntryUpdateDTO dto = TimeEntryUpdateDTO.builder()
                .employeeId( recordId.toString() )
                .projectId( recordId.toString() )
                .date( timeEntry.getDate() )
                .description( timeEntry.getDescription() )
                .totalTime( timeEntry.getTotalTime() )
                .pauseTime( timeEntry.getPauseTime() )
                .build();

        when( timeEntryRepository.findById( recordId ) ).thenReturn( Optional.of( timeEntry ) );
        when( projectAssignmentRepository.findByProject_IdAndEmployee_Id( recordId, recordId ) )
                .thenReturn( Optional.of( projectAssignment ) );

        when( timeEntryMapper.toTimeEntry( dto, employee, project, timeEntry ) ).thenReturn( timeEntry );

        when( calculationService.calculateTotal( dto.getTotalTime(), dto.getPauseTime(), projectAssignment.getHourlyRate() ) )
                .thenReturn( BigDecimal.valueOf( 8 * projectAssignment.getHourlyRate().longValue() ) );

        when( calculationService.calculateTotal( dto.getTotalTime(), dto.getPauseTime(), internalRate ) )
                .thenReturn( BigDecimal.valueOf( 8 * 30 ) );

        when( timeEntryRepository.save( timeEntry ) ).thenReturn( timeEntry );

        when( timeEntryMapper.toTimeEntryDTO( timeEntry ) ).thenReturn( timeEntryDTO );

        TimeEntryDTO actual = assertDoesNotThrow( () -> service.updateTimeEntry(
                recordId.toString(),
                dto
        ) );

        assertEquals( timeEntryDTO, actual );

        verify( timeEntryRepository, times( 1 ) ).findById( recordId );
        verify( projectAssignmentRepository, times( 1 ) )
                .findByProject_IdAndEmployee_Id( recordId, recordId );

        verify( timeEntryMapper, times( 1 ) ).toTimeEntry( dto, employee, project, timeEntry );
        verify( calculationService, times( 1 ) ).calculateTotal( dto.getTotalTime(), dto.getPauseTime(), projectAssignment.getHourlyRate() );
        verify( calculationService, times( 1 ) ).calculateTotal( dto.getTotalTime(), dto.getPauseTime(), internalRate );
        verify( timeEntryRepository, times( 1 ) ).save( timeEntry );
        verify( timeEntryMapper, times( 1 ) ).toTimeEntryDTO( timeEntry );
    }

    @Test
    @DisplayName("updateTimeEntry should return time entry dto when hourly rate is zero")
    void updateTimeEntry_ShouldReturnTimeEntryDTOInstance_WhenHourlyRateIsZero() {
        assert employee.getId() != null;
        assert project.getId() != null;
        TimeEntryUpdateDTO dto = TimeEntryUpdateDTO.builder()
                .date( timeEntry.getDate() )
                .description( timeEntry.getDescription() )
                .totalTime( timeEntry.getTotalTime() )
                .pauseTime( timeEntry.getPauseTime() )
                .build();

        projectAssignment.setHourlyRate( BigDecimal.ZERO );

        when( timeEntryRepository.findById( recordId ) ).thenReturn( Optional.of( timeEntry ) );

        when( timeEntryMapper.toTimeEntry( dto, employee, project, timeEntry ) ).thenReturn( timeEntry );

        when( calculationService.calculateTotal( dto.getTotalTime(), dto.getPauseTime(), rate ) )
                .thenReturn( BigDecimal.valueOf( 8 * rate.longValue() ) );

        when( calculationService.calculateTotal( dto.getTotalTime(), dto.getPauseTime(), internalRate ) )
                .thenReturn( BigDecimal.valueOf( 8 * 30 ) );

        when( timeEntryRepository.save( timeEntry ) ).thenReturn( timeEntry );

        when( timeEntryMapper.toTimeEntryDTO( timeEntry ) ).thenReturn( timeEntryDTO );

        TimeEntryDTO actual = assertDoesNotThrow( () -> service.updateTimeEntry(
                recordId.toString(),
                dto
        ) );

        assertEquals( timeEntryDTO, actual );

        verify( timeEntryRepository, times( 1 ) ).findById( recordId );
        verify( timeEntryMapper, times( 1 ) ).toTimeEntry( dto, employee, project, timeEntry );
        verify( calculationService, times( 1 ) ).calculateTotal( dto.getTotalTime(), dto.getPauseTime(), rate );
        verify( calculationService, times( 1 ) ).calculateTotal( dto.getTotalTime(), dto.getPauseTime(), internalRate );
        verify( timeEntryRepository, times( 1 ) ).save( timeEntry );
        verify( timeEntryMapper, times( 1 ) ).toTimeEntryDTO( timeEntry );
    }

    @Test
    @DisplayName("updateOwnTimeEntry should throw when related employee not found")
    void updateOwnTimeEntry_ShouldThrow_WhenRelatedEmployeeNotFound() {
        assert employee.getId() != null;
        assert project.getId() != null;
        TimeEntryUpdateDTO dto = TimeEntryUpdateDTO.builder()
                .date( timeEntry.getDate() )
                .description( timeEntry.getDescription() )
                .totalTime( timeEntry.getTotalTime() )
                .pauseTime( timeEntry.getPauseTime() )
                .build();

        when( employeeRepository.findByUser_Id( otherRecordId ) ).thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class, () -> service.updateOwnTimeEntry(
                recordId.toString(),
                dto,
                new SecurityUser( otherUser )
        ) );

        verify( employeeRepository, times( 1 ) ).findByUser_Id( otherRecordId );
    }

    @Test
    @DisplayName("updateOwnTimeEntry should throw when time entry not found")
    void updateOwnTimeEntry_ShouldThrow_WhenTimeEntryNotFound() {
        assert employee.getId() != null;
        assert project.getId() != null;

        TimeEntryUpdateDTO dto = TimeEntryUpdateDTO.builder()
                .date( timeEntry.getDate() )
                .description( timeEntry.getDescription() )
                .totalTime( timeEntry.getTotalTime() )
                .pauseTime( timeEntry.getPauseTime() )
                .build();

        when( employeeRepository.findByUser_Id( recordId ) ).thenReturn( Optional.of( employee ) );
        when( timeEntryRepository.findById( notExistingRecordId ) ).thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class, () -> service.updateOwnTimeEntry(
                notExistingRecordId.toString(),
                dto,
                new SecurityUser( user )
        ) );

        verify( employeeRepository, times( 1 ) ).findByUser_Id( recordId );
        verify( timeEntryRepository, times( 1 ) ).findById( notExistingRecordId );
    }

    @Test
    @DisplayName("updateOwnTimeEntry should throw when user does not own time entry")
    void updateOwnTimeEntry_ShouldThrow_WhenUserDoesNotOwnTimeEntry() {
        assert employee.getId() != null;
        assert project.getId() != null;
        TimeEntryUpdateDTO dto = TimeEntryUpdateDTO.builder()
                .date( timeEntry.getDate() )
                .description( timeEntry.getDescription() )
                .totalTime( timeEntry.getTotalTime() )
                .pauseTime( timeEntry.getPauseTime() )
                .build();

        when( employeeRepository.findByUser_Id( otherRecordId ) ).thenReturn( Optional.of( otherEmployee ) );
        when( timeEntryRepository.findById( recordId ) ).thenReturn( Optional.of( timeEntry ) );

        assertThrows( AccessDeniedException.class, () -> service.updateOwnTimeEntry(
                recordId.toString(),
                dto,
                new SecurityUser( otherUser )
        ) );

        verify( employeeRepository, times( 1 ) ).findByUser_Id( otherRecordId );
        verify( timeEntryRepository, times( 1 ) ).findById( recordId );
    }

    @Test
    @DisplayName("updateOwnTimeEntry should return update time entry dto")
    void updateOwnTimeEntry_ShouldReturnTimeEntryDTOInstance_WhenCalled() {
        assert employee.getId() != null;
        assert project.getId() != null;
        TimeEntryUpdateDTO dto = TimeEntryUpdateDTO.builder()
                .date( timeEntry.getDate().plusDays( 1 ) )
                .description( timeEntry.getDescription() )
                .totalTime( timeEntry.getTotalTime() )
                .pauseTime( timeEntry.getPauseTime() )
                .build();

        when( employeeRepository.findByUser_Id( recordId ) ).thenReturn( Optional.of( employee ) );

        when( timeEntryRepository.findById( recordId ) ).thenReturn( Optional.of( timeEntry ) );

        when( timeEntryMapper.toTimeEntry( dto, employee, project, timeEntry ) ).thenReturn( timeEntry );

        when( calculationService.calculateTotal( dto.getTotalTime(), dto.getPauseTime(), projectAssignment.getHourlyRate() ) )
                .thenReturn( BigDecimal.valueOf( 8 * projectAssignment.getHourlyRate().longValue() ) );

        when( calculationService.calculateTotal( dto.getTotalTime(), dto.getPauseTime(), internalRate ) )
                .thenReturn( BigDecimal.valueOf( 8 * 30 ) );

        when( timeEntryRepository.save( timeEntry ) ).thenReturn( timeEntry );

        when( timeEntryMapper.toTimeEntryDTO( timeEntry ) ).thenReturn( timeEntryDTO );

        TimeEntryDTO actual = assertDoesNotThrow( () -> service.updateOwnTimeEntry(
                recordId.toString(),
                dto,
                new SecurityUser( user )
        ) );

        assertEquals( timeEntryDTO, actual );

        verify( employeeRepository, times( 1 ) ).findByUser_Id( recordId );
        verify( timeEntryRepository, times( 1 ) ).findById( recordId );
        verify( timeEntryMapper, times( 1 ) ).toTimeEntry( dto, employee, project, timeEntry );
        verify( calculationService, times( 1 ) ).calculateTotal( dto.getTotalTime(), dto.getPauseTime(), projectAssignment.getHourlyRate() );
        verify( calculationService, times( 1 ) ).calculateTotal( dto.getTotalTime(), dto.getPauseTime(), internalRate );
        verify( timeEntryRepository, times( 1 ) ).save( timeEntry );
        verify( timeEntryMapper, times( 1 ) ).toTimeEntryDTO( timeEntry );
    }

    @Test
    @DisplayName("updateOwnTimeEntry should return update time entry dto")
    void updateOwnTimeEntry_ShouldReturnTimeEntryDTOInstance_WhenCalledWithTimeRange() {
        assert employee.getId() != null;
        assert project.getId() != null;
        Instant now = Instant.now();
        TimeEntryUpdateDTO dto = TimeEntryUpdateDTO.builder()
                .date( timeEntry.getDate().plusDays( 1 ) )
                .description( timeEntry.getDescription() )
                .startTime( now )
                .endTime( now.plus( 10, ChronoUnit.HOURS ) )
                .totalTime( Duration.ofHours( 10 ) )
                .pauseTime( Duration.ofHours( 2 ) )
                .build();

        when( employeeRepository.findByUser_Id( recordId ) ).thenReturn( Optional.of( employee ) );

        when( timeEntryRepository.findById( recordId ) ).thenReturn( Optional.of( timeEntry ) );

        when( timeEntryMapper.toTimeEntry( dto, employee, project, timeEntry ) ).thenReturn( timeEntry );

        when( calculationService.calculateTotal( dto.getTotalTime(), dto.getPauseTime(), projectAssignment.getHourlyRate() ) )
                .thenReturn( BigDecimal.valueOf( 8 * projectAssignment.getHourlyRate().longValue() ) );

        when( calculationService.calculateTotal( dto.getTotalTime(), dto.getPauseTime(), internalRate ) )
                .thenReturn( BigDecimal.valueOf( 8 * 30 ) );

        when( timeEntryRepository.save( timeEntry ) ).thenReturn( timeEntry );

        when( timeEntryMapper.toTimeEntryDTO( timeEntry ) ).thenReturn( timeEntryDTO );

        TimeEntryDTO actual = assertDoesNotThrow( () -> service.updateOwnTimeEntry(
                recordId.toString(),
                dto,
                new SecurityUser( user )
        ) );

        assertEquals( timeEntryDTO, actual );

        verify( employeeRepository, times( 1 ) ).findByUser_Id( recordId );
        verify( timeEntryRepository, times( 1 ) ).findById( recordId );
        verify( timeEntryMapper, times( 1 ) ).toTimeEntry( dto, employee, project, timeEntry );
        verify( calculationService, times( 1 ) ).calculateTotal( dto.getTotalTime(), dto.getPauseTime(), projectAssignment.getHourlyRate() );
        verify( calculationService, times( 1 ) ).calculateTotal( dto.getTotalTime(), dto.getPauseTime(), internalRate );
        verify( timeEntryRepository, times( 1 ) ).save( timeEntry );
        verify( timeEntryMapper, times( 1 ) ).toTimeEntryDTO( timeEntry );
    }

    @Test
    @DisplayName("deleteTimeEntry should throw when time entry does not exist in db")
    void deleteTimeEntry_ShouldThrow_WhenTimeEntryDoesNotExistInDB() {
        when( timeEntryRepository.findById( notExistingRecordId ) ).thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class,
                () -> service.deleteTimeEntryById( notExistingRecordId.toString() )
        );

        verify( timeEntryRepository, times( 1 ) ).findById( notExistingRecordId );
    }

    @Test
    @DisplayName("deleteTimeEntry should delete time entry when time entry exists in db")
    void deleteTimeEntry_ShouldDeleteTimeEntry_WhenTimeEntryExistsInDB() {
        when( timeEntryRepository.findById( recordId ) ).thenReturn( Optional.of( timeEntry ) );

        assertDoesNotThrow( () -> service.deleteTimeEntryById( recordId.toString() ) );

        verify( timeEntryRepository, times( 1 ) ).findById( recordId );
        verify( timeEntryRepository, times( 1 ) ).delete( timeEntry );
    }

    @Test
    @DisplayName("deleteOwnTimeEntry should throw when employee not found")
    void deleteOwnTimeEntry_ShouldThrow_WhenEmployeeNotFound() {
        when( employeeRepository.findByUser_Id( notExistingRecordId ) ).thenReturn( Optional.empty() );

        otherUser.setId( notExistingRecordId );

        assertThrows( EntityNotFoundException.class,
                () -> service.deleteOwnTimeEntryById( recordId.toString(), new SecurityUser( otherUser ) )
        );

        verify( employeeRepository, times( 1 ) ).findByUser_Id( notExistingRecordId );
    }

    @Test
    @DisplayName("deleteOwnTimeEntry should throw when user does not own time entry")
    void deleteOwnTimeEntry_ShouldThrow_WhenUserDoesNotOwnTimeEntry() {
        when( employeeRepository.findByUser_Id( otherRecordId ) ).thenReturn( Optional.of( otherEmployee ) );

        when( timeEntryRepository.findById( recordId ) ).thenReturn( Optional.of( timeEntry ) );

        assertThrows( AccessDeniedException.class,
                () -> service.deleteOwnTimeEntryById( recordId.toString(), new SecurityUser( otherUser ) )
        );

        verify( employeeRepository, times( 1 ) ).findByUser_Id( otherRecordId );
        verify( timeEntryRepository, times( 1 ) ).findById( recordId );
    }

    @Test
    @DisplayName("deleteOwnTimeEntry should throw when time entry not found")
    void deleteOwnTimeEntry_ShouldThrow_WhenTimeEntryNotFound() {
        when( employeeRepository.findByUser_Id( recordId ) ).thenReturn( Optional.of( employee ) );

        when( timeEntryRepository.findById( notExistingRecordId ) ).thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class,
                () -> service.deleteOwnTimeEntryById( notExistingRecordId.toString(), new SecurityUser( user ) )
        );

        verify( employeeRepository, times( 1 ) ).findByUser_Id( recordId );
        verify( timeEntryRepository, times( 1 ) ).findById( notExistingRecordId );
    }

    @Test
    @DisplayName("deleteOwnTimeEntry should delete time entry")
    void deleteOwnTimeEntry_ShouldDeleteTimeEntry_WhenCalled() {
        when( employeeRepository.findByUser_Id( recordId ) ).thenReturn( Optional.of( employee ) );

        when( timeEntryRepository.findById( recordId ) ).thenReturn( Optional.of( timeEntry ) );

        assertDoesNotThrow( () ->
                service.deleteOwnTimeEntryById( recordId.toString(), new SecurityUser( user ) )
        );

        verify( employeeRepository, times( 1 ) ).findByUser_Id( recordId );
        verify( timeEntryRepository, times( 1 ) ).findById( recordId );
    }

}