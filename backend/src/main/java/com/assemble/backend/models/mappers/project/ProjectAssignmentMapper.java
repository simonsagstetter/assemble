/*
 * assemble
 * ProjectAssignmentMapper.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.mappers.project;

import com.assemble.backend.models.dtos.project.ProjectAssignmentCreateDTO;
import com.assemble.backend.models.dtos.project.ProjectAssignmentDTO;
import com.assemble.backend.models.entities.employee.Employee;
import com.assemble.backend.models.entities.project.Project;
import com.assemble.backend.models.entities.project.ProjectAssignment;
import com.assemble.backend.models.mappers.core.BaseEntity;
import com.assemble.backend.models.mappers.employee.EmployeeMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { EmployeeMapper.class, ProjectMapper.class }
)
public interface ProjectAssignmentMapper {

    @BaseEntity
    ProjectAssignmentDTO toProjectAssignmentDTO( ProjectAssignment projectAssignment );

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "employee", source = "employee")
    @Mapping(target = "project", source = "project")
    @Mapping(target = "active", source = "projectAssignmentCreateDTO.active")
    @Mapping(target = "hourlyRate", source = "projectAssignmentCreateDTO.hourlyRate")
    ProjectAssignment toProjectAssignment( ProjectAssignmentCreateDTO projectAssignmentCreateDTO,
                                           Employee employee,
                                           Project project
    );
}
