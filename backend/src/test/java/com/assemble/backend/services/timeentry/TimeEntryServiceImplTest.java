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

import com.assemble.backend.models.dtos.employee.EmployeeDTO;
import com.assemble.backend.models.dtos.project.ProjectDTO;
import com.assemble.backend.models.dtos.timeentry.TimeEntryCreateDTO;
import com.assemble.backend.models.dtos.timeentry.TimeEntryDTO;
import com.assemble.backend.models.entities.auth.UserAudit;
import com.assemble.backend.models.entities.employee.Employee;
import com.assemble.backend.models.entities.project.Project;
import com.assemble.backend.models.entities.project.ProjectStage;
import com.assemble.backend.models.entities.project.ProjectType;
import com.assemble.backend.models.entities.timeentry.TimeEntry;
import com.assemble.backend.models.mappers.timeentry.TimeEntryMapper;
import com.assemble.backend.repositories.employee.EmployeeRepository;
import com.assemble.backend.repositories.project.ProjectRepository;
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

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
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
    private ProjectRepository projectRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private TimeEntryCalculationService calculationService;

    @InjectMocks
    private TimeEntryServiceImpl service;

    private static Project project;
    private static ProjectDTO projectDTO;
    private static Employee employee;
    private static EmployeeDTO employeeDTO;
    private static TimeEntry timeEntry;
    private static TimeEntryDTO timeEntryDTO;

    private static UUID recordId;
    private static UUID notExistingRecordId;

    private static final BigDecimal rate = BigDecimal.valueOf( 50 );
    private static final BigDecimal internalRate = BigDecimal.valueOf( 30 );

    @BeforeEach
    void init() {
        UserAudit userAudit = new UserAudit( null, "SYSTEM" );
        Instant now = Instant.now();

        recordId = UuidCreator.getTimeOrderedEpoch();
        notExistingRecordId = UuidCreator.getTimeOrderedEpoch();

        project = Project.builder()
                .id( recordId )
                .no( "P000001" )
                .name( "Test Project" )
                .description( "Test Project Description" )
                .category( "Maintanance" )
                .stage( ProjectStage.PROPOSAL )
                .type( ProjectType.EXTERNAL )
                .createdDate( now )
                .lastModifiedDate( now )
                .createdBy( userAudit )
                .lastModifiedBy( userAudit )
                .build();

        projectDTO = ProjectDTO.builder()
                .id( recordId.toString() )
                .no( project.getNo() )
                .name( project.getName() )
                .active( project.isActive() )
                .stage( project.getStage() )
                .type( project.getType() )
                .description( project.getDescription() )
                .category( project.getCategory() )
                .createdDate( project.getCreatedDate() )
                .lastModifiedDate( project.getLastModifiedDate() )
                .createdBy( project.getCreatedBy() )
                .lastModifiedBy( project.getLastModifiedBy() )
                .build();

        employee = Employee.builder()
                .id( recordId )
                .no( "E000001" )
                .firstname( "Max" )
                .lastname( "Mustermann" )
                .email( "max.mustermann@example.com" )
                .user( null )
                .createdDate( now )
                .lastModifiedDate( now )
                .createdBy( userAudit )
                .lastModifiedBy( userAudit )
                .build();

        employeeDTO = EmployeeDTO.builder()
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
                .user( null )
                .build();

        timeEntry = TimeEntry.builder()
                .id( recordId )
                .no( "T000001" )
                .project( project )
                .employee( employee )
                .description( "Test Time Entry Description" )
                .rate( rate )
                .total( BigDecimal.valueOf( 8 * 450 ) )
                .totalInternal( BigDecimal.valueOf( 8 * 30 ) )
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
    @DisplayName("createTimeEntry should throw when project does not exist in db")
    void createTimeEntry_ShouldThrow_WhenProjectDoesNotExistInDB() {
        TimeEntryCreateDTO dto = TimeEntryCreateDTO.builder()
                .projectId( notExistingRecordId.toString() )
                .employeeId( recordId.toString() )
                .description( "Test description" )
                .build();

        when( projectRepository.findById( notExistingRecordId ) ).thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class, () -> service.createTimeEntry( dto ) );

        verify( projectRepository, times( 1 ) ).findById( notExistingRecordId );
    }

    @Test
    @DisplayName("createTimeEntry should throw when employee does not exist in db")
    void createTimeEntry_ShouldThrow_WhenEmployeeDoesNotExistInDB() {
        TimeEntryCreateDTO dto = TimeEntryCreateDTO.builder()
                .projectId( recordId.toString() )
                .employeeId( notExistingRecordId.toString() )
                .description( "Test description" )
                .build();

        when( projectRepository.findById( recordId ) ).thenReturn( Optional.of( project ) );
        when( employeeRepository.findById( notExistingRecordId ) ).thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class, () -> service.createTimeEntry( dto ) );

        verify( projectRepository, times( 1 ) ).findById( recordId );
        verify( employeeRepository, times( 1 ) ).findById( notExistingRecordId );
    }

    @Test
    @DisplayName("createTimeEntry should return instance of time entry dto when called with time range")
    void createTimeEntry_ShouldReturnTimeEntryDTOInstance_WhenCalledWithTimeRange() {
        TimeEntryCreateDTO dto = TimeEntryCreateDTO.builder()
                .projectId( recordId.toString() )
                .employeeId( recordId.toString() )
                .description( "Test description" )
                .startTime( Instant.now() )
                .endTime( Instant.now().plusSeconds( 32400 ) )
                .pauseTime( Duration.ofSeconds( 3600 ) )
                .totalTime( null )
                .build();

        timeEntry.setTotalTime( Duration.ofSeconds( 32400 ) );
        timeEntry.setPauseTime( dto.getPauseTime() );

        when( projectRepository.findById( recordId ) ).thenReturn( Optional.of( project ) );
        when( employeeRepository.findById( recordId ) ).thenReturn( Optional.of( employee ) );

        when( timeEntryMapper.toTimeEntry( dto, employee, project ) ).thenReturn( timeEntry );

        when( calculationService
                .calculateTotal( timeEntry.getTotalTime(), timeEntry.getPauseTime(), rate ) )
                .thenReturn( new BigDecimal( 360 ) );
        when( calculationService
                .calculateTotal( timeEntry.getTotalTime(), timeEntry.getPauseTime(), internalRate ) )
                .thenReturn( new BigDecimal( 240 ) );
        when( timeEntryRepository.save( timeEntry ) ).thenReturn( timeEntry );
        when( timeEntryMapper.toTimeEntryDTO( timeEntry ) ).thenReturn( timeEntryDTO );

        TimeEntryDTO actual = assertDoesNotThrow( () -> service.createTimeEntry( dto ) );

        assertNotNull( actual );

        verify( projectRepository, times( 1 ) ).findById( recordId );
        verify( employeeRepository, times( 1 ) ).findById( recordId );
        verify( timeEntryMapper, times( 1 ) ).toTimeEntry( dto, employee, project );
        verify( calculationService, times( 1 ) )
                .calculateTotal( timeEntry.getTotalTime(), timeEntry.getPauseTime(), rate );
        verify( calculationService, times( 1 ) )
                .calculateTotal( timeEntry.getTotalTime(), timeEntry.getPauseTime(), internalRate );
        verify( timeEntryRepository, times( 1 ) ).save( timeEntry );
        verify( timeEntryMapper, times( 1 ) ).toTimeEntryDTO( timeEntry );
    }

    @Test
    @DisplayName("createTimeEntry should return instance of time entry dto when called with duration")
    void createTimeEntry_ShouldReturnTimeEntryDTOInstance_WhenCalledWithDuration() {
        TimeEntryCreateDTO dto = TimeEntryCreateDTO.builder()
                .projectId( recordId.toString() )
                .employeeId( recordId.toString() )
                .description( "Test description" )
                .startTime( null )
                .endTime( null )
                .totalTime( Duration.ofSeconds( 32400 ) )
                .pauseTime( Duration.ofSeconds( 3600 ) )
                .build();

        timeEntry.setTotalTime( dto.getTotalTime() );
        timeEntry.setPauseTime( dto.getPauseTime() );

        when( projectRepository.findById( recordId ) ).thenReturn( Optional.of( project ) );
        when( employeeRepository.findById( recordId ) ).thenReturn( Optional.of( employee ) );
        when( timeEntryMapper.toTimeEntry( dto, employee, project ) ).thenReturn( timeEntry );
        when( calculationService
                .calculateTotal( timeEntry.getTotalTime(), timeEntry.getPauseTime(), rate ) )
                .thenReturn( new BigDecimal( 360 ) );
        when( calculationService
                .calculateTotal( timeEntry.getTotalTime(), timeEntry.getPauseTime(), internalRate ) )
                .thenReturn( new BigDecimal( 240 ) );
        when( timeEntryRepository.save( timeEntry ) ).thenReturn( timeEntry );
        when( timeEntryMapper.toTimeEntryDTO( timeEntry ) ).thenReturn( timeEntryDTO );

        TimeEntryDTO actual = assertDoesNotThrow( () -> service.createTimeEntry( dto ) );

        assertNotNull( actual );

        verify( projectRepository, times( 1 ) ).findById( recordId );
        verify( employeeRepository, times( 1 ) ).findById( recordId );
        verify( timeEntryMapper, times( 1 ) ).toTimeEntry( dto, employee, project );
        verify( calculationService, times( 1 ) )
                .calculateTotal( timeEntry.getTotalTime(), timeEntry.getPauseTime(), rate );
        verify( calculationService, times( 1 ) )
                .calculateTotal( timeEntry.getTotalTime(), timeEntry.getPauseTime(), internalRate );
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
}