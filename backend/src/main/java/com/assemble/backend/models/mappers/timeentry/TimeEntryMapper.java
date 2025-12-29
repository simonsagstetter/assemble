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
import com.assemble.backend.models.entities.employee.Employee;
import com.assemble.backend.models.entities.project.Project;
import com.assemble.backend.models.entities.timeentry.TimeEntry;
import com.assemble.backend.models.mappers.core.BaseEntity;
import com.assemble.backend.models.mappers.employee.EmployeeMapper;
import com.assemble.backend.models.mappers.project.ProjectMapper;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.WARN,
        uses = { ProjectMapper.class, EmployeeMapper.class }
)
public interface TimeEntryMapper {

    @BaseEntity
    TimeEntryDTO toTimeEntryDTO( TimeEntry timeEntry );

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "employee", source = "e")
    @Mapping(target = "project", source = "p")
    @Mapping(target = "description", source = "timeEntryCreateDTO.description")
    @Mapping(target = "startTime", source = "timeEntryCreateDTO.startTime")
    @Mapping(target = "endTime", source = "timeEntryCreateDTO.endTime")
    @Mapping(target = "totalTime", source = "timeEntryCreateDTO.totalTime")
    @Mapping(target = "pauseTime", source = "timeEntryCreateDTO.pauseTime")
    TimeEntry toTimeEntry( TimeEntryCreateDTO timeEntryCreateDTO, Employee e, Project p );
}
