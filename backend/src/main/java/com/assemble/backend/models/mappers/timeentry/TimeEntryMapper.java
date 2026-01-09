/*
 * assemble
 * TimeEntryMapper.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.mappers.timeentry;

import com.assemble.backend.models.dtos.timeentry.TimeEntryCreateDTO;
import com.assemble.backend.models.dtos.timeentry.TimeEntryDTO;
import com.assemble.backend.models.dtos.timeentry.TimeEntryUpdateDTO;
import com.assemble.backend.models.entities.employee.Employee;
import com.assemble.backend.models.entities.project.Project;
import com.assemble.backend.models.entities.timeentry.TimeEntry;
import com.assemble.backend.models.mappers.core.BaseEntity;
import com.assemble.backend.models.mappers.employee.EmployeeMapper;
import com.assemble.backend.models.mappers.project.ProjectMapper;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = { ProjectMapper.class, EmployeeMapper.class }
)
public interface TimeEntryMapper {

    @BaseEntity
    TimeEntryDTO toTimeEntryDTO( TimeEntry timeEntry );

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "employee", source = "e")
    @Mapping(target = "project", source = "p")
    @Mapping(target = "description", source = "timeEntryCreateDTO.description")
    @Mapping(target = "date", source = "timeEntryCreateDTO.date")
    @Mapping(target = "startTime", source = "timeEntryCreateDTO.startTime")
    @Mapping(target = "endTime", source = "timeEntryCreateDTO.endTime")
    @Mapping(target = "totalTime", source = "timeEntryCreateDTO.totalTime")
    @Mapping(target = "pauseTime", source = "timeEntryCreateDTO.pauseTime")
    TimeEntry toTimeEntry( TimeEntryCreateDTO timeEntryCreateDTO, Employee e, Project p );

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, ignoreByDefault = true)
    @Mapping(target = "employee", source = "e")
    @Mapping(target = "project", source = "p")
    @Mapping(target = "description", source = "timeEntryUpdateDTO.description")
    @Mapping(target = "date", source = "timeEntryUpdateDTO.date")
    @Mapping(target = "startTime", source = "timeEntryUpdateDTO.startTime")
    @Mapping(target = "endTime", source = "timeEntryUpdateDTO.endTime")
    @Mapping(target = "totalTime", source = "timeEntryUpdateDTO.totalTime")
    @Mapping(target = "pauseTime", source = "timeEntryUpdateDTO.pauseTime")
    TimeEntry toTimeEntry( TimeEntryUpdateDTO timeEntryUpdateDTO, Employee e, Project p, @MappingTarget TimeEntry timeEntry );
}
