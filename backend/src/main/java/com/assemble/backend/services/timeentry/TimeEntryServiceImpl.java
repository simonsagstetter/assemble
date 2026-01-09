/*
 * assemble
 * TimeEntryServiceImpl.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.timeentry;

import com.assemble.backend.models.dtos.timeentry.TimeEntryCreateDTO;
import com.assemble.backend.models.dtos.timeentry.TimeEntryDTO;
import com.assemble.backend.models.dtos.timeentry.TimeEntryUpdateDTO;
import com.assemble.backend.models.dtos.timeentry.validators.TimeValidatable;
import com.assemble.backend.models.entities.auth.SecurityUser;
import com.assemble.backend.models.entities.employee.Employee;
import com.assemble.backend.models.entities.project.Project;
import com.assemble.backend.models.entities.project.ProjectAssignment;
import com.assemble.backend.models.entities.timeentry.TimeEntry;
import com.assemble.backend.models.mappers.timeentry.TimeEntryMapper;
import com.assemble.backend.repositories.employee.EmployeeRepository;
import com.assemble.backend.repositories.project.ProjectAssignmentRepository;
import com.assemble.backend.repositories.timeentry.TimeEntryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
public class TimeEntryServiceImpl implements TimeEntryService {

    private static final BigDecimal FIXED_RATE = new BigDecimal( 50 );
    private static final BigDecimal FIXED_INTERNAL_RATE = new BigDecimal( 30 );

    private TimeEntryRepository timeEntryRepository;
    private TimeEntryMapper timeEntryMapper;
    private TimeEntryCalculationService calculationService;

    private ProjectAssignmentRepository projectAssignmentRepository;
    private EmployeeRepository employeeRepository;

    @Override
    public List<TimeEntryDTO> getAllTimeEntries() {
        return timeEntryRepository.findAll()
                .stream()
                .map( timeEntryMapper::toTimeEntryDTO )
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimeEntryDTO> getTimeEntriesByEmployeeId( String employeeId ) {
        return timeEntryRepository.findAllByEmployeeId( UUID.fromString( employeeId ) )
                .stream()
                .map( timeEntryMapper::toTimeEntryDTO )
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimeEntryDTO> getTimeEntriesByProjectId( String projectId ) {
        return timeEntryRepository.findAllByProjectId( UUID.fromString( projectId ) )
                .stream()
                .map( timeEntryMapper::toTimeEntryDTO )
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimeEntryDTO> getOwnTimeEntries( SecurityUser user, String aroundDate ) {
        Employee employee = employeeRepository.findByUser_Id( user.getUser().getId() )
                .orElse( null );

        if ( employee != null && aroundDate != null ) {
            AtomicReference<LocalDate> date = new AtomicReference<>();

            try {
                LocalDate parsedDate = LocalDate.parse( aroundDate );
                date.set( parsedDate );
            } catch ( DateTimeParseException e ) {
                throw new InvalidParameterException( "Invalid date format" );
            }

            LocalDate afterDate = date.get().minusMonths( 2 );
            LocalDate beforeDate = date.get().plusMonths( 2 );

            return timeEntryRepository.findAllByEmployee_IdAndDateIsBetween(
                            employee.getId(), afterDate, beforeDate
                    )
                    .stream()
                    .map( timeEntryMapper::toTimeEntryDTO )
                    .toList();
        }

        return List.of();
    }

    @Override
    public TimeEntryDTO getTimeEntryById( String id ) {
        TimeEntry timeEntry = timeEntryRepository.findById( UUID.fromString( id ) )
                .orElseThrow(
                        () -> new EntityNotFoundException( "Could not find time entry with id: " + id )
                );
        return timeEntryMapper.toTimeEntryDTO( timeEntry );
    }

    @Override
    public TimeEntryDTO getOwnTimeEntryById( String id, SecurityUser user ) {
        TimeEntry timeEntry = timeEntryRepository.findById( UUID.fromString( id ) )
                .orElseThrow(
                        () -> new EntityNotFoundException( "Could not find time entry with id: " + id )
                );
        Employee employee = employeeRepository.findByUser_Id( user.getUser().getId() )
                .orElseThrow(
                        () -> new EntityNotFoundException( "Could not find employee for user with id: " + user.getUser().getId() )
                );

        if ( timeEntry.getEmployee().getId() != null &&
                !timeEntry.getEmployee().getId().equals( employee.getId() )
        ) {
            throw new AccessDeniedException( "You are not allowed to access this time entry" );
        }

        return timeEntryMapper.toTimeEntryDTO( timeEntry );
    }

    private TimeEntry setRateAndTotals( TimeEntry timeEntry, TimeValidatable timeValidatable, BigDecimal rate ) {
        if ( timeValidatable.getStartTime() != null && timeValidatable.getEndTime() != null ) {
            Duration d = Duration.between(
                    timeValidatable.getStartTime().truncatedTo( ChronoUnit.SECONDS ),
                    timeValidatable.getEndTime().truncatedTo( ChronoUnit.SECONDS )
            );

            timeEntry.setTotal(
                    calculationService.calculateTotal(
                            d,
                            timeValidatable.getPauseTime(),
                            rate
                    )
            );
            timeEntry.setRate( rate );
            timeEntry.setTotalInternal(
                    calculationService.calculateTotal(
                            d,
                            timeValidatable.getPauseTime(),
                            FIXED_INTERNAL_RATE
                    )
            );
            timeEntry.setTotalTime( d );

        } else {
            timeEntry.setTotal(
                    calculationService.calculateTotal(
                            timeValidatable.getTotalTime(),
                            timeValidatable.getPauseTime(),
                            rate
                    )
            );
            timeEntry.setRate( rate );
            timeEntry.setTotalInternal(
                    calculationService.calculateTotal(
                            timeValidatable.getTotalTime(),
                            timeValidatable.getPauseTime(),
                            FIXED_INTERNAL_RATE
                    )
            );
        }

        return timeEntry;
    }

    private TimeEntryDTO processTimeEntryCreation( TimeEntryCreateDTO timeEntryCreateDTO ) {
        ProjectAssignment projectAssignment = projectAssignmentRepository
                .findByProject_IdAndEmployee_Id(
                        UUID.fromString( timeEntryCreateDTO.getProjectId() ),
                        UUID.fromString( timeEntryCreateDTO.getEmployeeId() )
                ).orElseThrow(
                        () -> new EntityNotFoundException( "Could not find assignment for project and employee" )
                );

        if ( !projectAssignment.isActive() )
            throw new InvalidParameterException( "Project assignment is not active" );

        Employee employee = projectAssignment.getEmployee();
        Project project = projectAssignment.getProject();

        TimeEntry timeEntry = timeEntryMapper.toTimeEntry( timeEntryCreateDTO, employee, project );

        BigDecimal rate = projectAssignment.getHourlyRate() != null && projectAssignment.getHourlyRate().compareTo( BigDecimal.ZERO ) > 0 ?
                projectAssignment.getHourlyRate()
                : FIXED_RATE;


        TimeEntry newTimeEntry = timeEntryRepository.save(
                setRateAndTotals( timeEntry, timeEntryCreateDTO, rate )
        );

        return timeEntryMapper.toTimeEntryDTO( newTimeEntry );
    }

    @Override
    @Transactional
    public TimeEntryDTO createOwnTimeEntry( TimeEntryCreateDTO timeEntryCreateDTO, SecurityUser user ) {
        Employee relatedEmployee = employeeRepository.findByUser_Id( user.getUser().getId() ).orElseThrow(
                () -> new EntityNotFoundException( "Could not find employee for user with id: " + user.getUser().getId() )
        );

        if ( relatedEmployee.getId() != null &&
                !timeEntryCreateDTO.getEmployeeId().equals( relatedEmployee.getId().toString() ) ) {
            throw new AccessDeniedException( "You are not allowed to access this time entry" );
        }


        return processTimeEntryCreation( timeEntryCreateDTO );
    }

    @Override
    @Transactional
    public TimeEntryDTO createTimeEntry( TimeEntryCreateDTO timeEntryCreateDTO ) {
        return processTimeEntryCreation( timeEntryCreateDTO );
    }

    private TimeEntryDTO processTimeEntryUpdate( TimeEntryUpdateDTO timeEntryUpdateDTO, TimeEntry timeEntry ) {
        Employee employee = timeEntry.getEmployee();
        Project project = timeEntry.getProject();

        BigDecimal rate = timeEntry.getRate() != null && timeEntry.getRate().compareTo( BigDecimal.ZERO ) > 0
                ? timeEntry.getRate()
                : FIXED_RATE;

        if ( timeEntryUpdateDTO.getEmployeeId() != null && timeEntryUpdateDTO.getProjectId() != null ) {
            ProjectAssignment projectAssignment = projectAssignmentRepository
                    .findByProject_IdAndEmployee_Id(
                            UUID.fromString( timeEntryUpdateDTO.getProjectId() ),
                            UUID.fromString( timeEntryUpdateDTO.getEmployeeId() )
                    ).orElseThrow(
                            () -> new EntityNotFoundException( "Could not find assignment for project and employee" )
                    );

            if ( !projectAssignment.isActive() )
                throw new InvalidParameterException( "Project assignment is not active" );

            rate = projectAssignment.getHourlyRate() != null && projectAssignment.getHourlyRate().compareTo( BigDecimal.ZERO ) > 0 ?
                    projectAssignment.getHourlyRate() : rate;

            employee = projectAssignment.getEmployee();
            project = projectAssignment.getProject();
        }

        if ( timeEntryUpdateDTO.getTotalTime() == null ) timeEntryUpdateDTO.setTotalTime( timeEntry.getTotalTime() );
        if ( timeEntryUpdateDTO.getPauseTime() == null ) timeEntryUpdateDTO.setPauseTime( timeEntry.getPauseTime() );

        TimeEntry updatedTimeEntry = timeEntryMapper.toTimeEntry( timeEntryUpdateDTO, employee, project, timeEntry );
        TimeEntry savedTimeEntry = timeEntryRepository.save( setRateAndTotals( updatedTimeEntry, timeEntryUpdateDTO, rate ) );


        return timeEntryMapper.toTimeEntryDTO( savedTimeEntry );
    }

    @Override
    @Transactional
    public TimeEntryDTO updateOwnTimeEntry( String id, TimeEntryUpdateDTO timeEntryUpdateDTO, SecurityUser user ) {
        Employee relatedEmployee = employeeRepository.findByUser_Id( user.getUser().getId() ).orElseThrow(
                () -> new EntityNotFoundException( "Could not find employee for user with id: " + user.getUser().getId() )
        );

        TimeEntry timeEntry = timeEntryRepository.findById( UUID.fromString( id ) )
                .orElseThrow(
                        () -> new EntityNotFoundException( "Could not find time entry with id: " + id )
                );

        if ( timeEntry.getEmployee().getId() != null && !
                timeEntry.getEmployee().getId().equals( relatedEmployee.getId() ) )
            throw new AccessDeniedException( "You are not allowed to access this time entry" );

        return processTimeEntryUpdate( timeEntryUpdateDTO, timeEntry );
    }

    @Override
    @Transactional
    public TimeEntryDTO updateTimeEntry( String id, TimeEntryUpdateDTO timeEntryUpdateDTO ) {
        TimeEntry timeEntry = timeEntryRepository.findById( UUID.fromString( id ) )
                .orElseThrow(
                        () -> new EntityNotFoundException( "Could not find time entry with id: " + id )
                );
        return processTimeEntryUpdate( timeEntryUpdateDTO, timeEntry );
    }

    @Override
    @Transactional
    public void deleteOwnTimeEntryById( String id, SecurityUser user ) {
        Employee relatedEmployee = employeeRepository.findByUser_Id( user.getUser().getId() ).orElseThrow(
                () -> new EntityNotFoundException( "Could not find employee for user with id: " + user.getUser().getId() )
        );

        TimeEntry timeEntry = timeEntryRepository.findById( UUID.fromString( id ) )
                .orElseThrow(
                        () -> new EntityNotFoundException( "Could not find time entry with id: " + id )
                );

        if ( timeEntry.getEmployee().getId() != null &&
                !timeEntry.getEmployee().getId().equals( relatedEmployee.getId() ) )
            throw new AccessDeniedException( "You are not allowed to access this time entry" );

        timeEntryRepository.delete( timeEntry );

    }

    @Override
    @Transactional
    public void deleteTimeEntryById( String id ) {
        TimeEntry timeEntry = timeEntryRepository.findById( UUID.fromString( id ) )
                .orElseThrow(
                        () -> new EntityNotFoundException( "Could not find time entry with id: " + id )
                );

        timeEntryRepository.delete( timeEntry );
    }
}
