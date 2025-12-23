/*
 * assemble
 * ProjectMapper.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.mappers.project;

import com.assemble.backend.models.dtos.project.ProjectCreateDTO;
import com.assemble.backend.models.dtos.project.ProjectDTO;
import com.assemble.backend.models.entities.project.Project;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface ProjectMapper {

    ProjectDTO toProjectDTO( Project project );

    Project toProject( ProjectCreateDTO projectCreateDTO );
}
