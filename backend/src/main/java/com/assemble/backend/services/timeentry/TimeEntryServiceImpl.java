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
import com.assemble.backend.models.entities.employee.Employee;
import com.assemble.backend.models.entities.project.Project;
import com.assemble.backend.models.entities.timeentry.TimeEntry;
import com.assemble.backend.models.mappers.timeentry.TimeEntryMapper;
import com.assemble.backend.repositories.employee.EmployeeRepository;
import com.assemble.backend.repositories.project.ProjectRepository;
import com.assemble.backend.repositories.timeentry.TimeEntryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TimeEntryServiceImpl implements TimeEntryService {

    private static final BigDecimal FIXED_RATE = new BigDecimal( 50 );
    private static final BigDecimal FIXED_INTERNAL_RATE = new BigDecimal( 30 );

    private TimeEntryRepository timeEntryRepository;
    private TimeEntryMapper timeEntryMapper;
    private TimeEntryCalculationService calculationService;

    private EmployeeRepository employeeRepository;
    private ProjectRepository projectRepository;

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
    public TimeEntryDTO getTimeEntryById( String id ) {
        TimeEntry timeEntry = timeEntryRepository.findById( UUID.fromString( id ) )
                .orElseThrow(
                        () -> new EntityNotFoundException( "Could not find time entry with id: " + id )
                );
        return timeEntryMapper.toTimeEntryDTO( timeEntry );
    }

    @Override
    @Transactional
    public TimeEntryDTO createTimeEntry( TimeEntryCreateDTO timeEntryCreateDTO ) {
        Project project = projectRepository.findById( UUID.fromString( timeEntryCreateDTO.getProjectId() ) )
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Could not find project with id: " + timeEntryCreateDTO.getProjectId()
                        )
                );

        Employee employee = employeeRepository.findById( UUID.fromString( timeEntryCreateDTO.getEmployeeId() ) )
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Could not find employee with id: " + timeEntryCreateDTO.getEmployeeId()
                        )
                );

        TimeEntry timeEntry = timeEntryMapper.toTimeEntry( timeEntryCreateDTO, employee, project );

        if ( timeEntryCreateDTO.getStartTime() != null && timeEntryCreateDTO.getEndTime() != null ) {
            Duration d = Duration.ofMillis(
                    timeEntryCreateDTO.getEndTime().toEpochMilli()
                            - timeEntryCreateDTO.getStartTime().toEpochMilli()
            );

            timeEntry.setTotal(
                    calculationService.calculateTotal(
                            d,
                            timeEntryCreateDTO.getPauseTime(),
                            FIXED_RATE
                    )
            );
            timeEntry.setRate( FIXED_RATE );
            timeEntry.setTotalInternal(
                    calculationService.calculateTotal(
                            d,
                            timeEntryCreateDTO.getPauseTime(),
                            FIXED_INTERNAL_RATE
                    )
            );
            timeEntry.setTotalTime( d );

        } else if ( timeEntryCreateDTO.getTotalTime() != null ) {

            timeEntry.setTotal(
                    calculationService.calculateTotal(
                            timeEntryCreateDTO.getTotalTime(),
                            timeEntryCreateDTO.getPauseTime(),
                            FIXED_RATE
                    )
            );
            timeEntry.setRate( FIXED_RATE );
            timeEntry.setTotalInternal(
                    calculationService.calculateTotal(
                            timeEntryCreateDTO.getTotalTime(),
                            timeEntryCreateDTO.getPauseTime(),
                            FIXED_INTERNAL_RATE
                    )
            );
        }

        TimeEntry newTimeEntry = timeEntryRepository.save( timeEntry );

        return timeEntryMapper.toTimeEntryDTO( newTimeEntry );
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
