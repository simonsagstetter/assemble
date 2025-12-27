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
import com.assemble.backend.models.mappers.core.BaseEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProjectMapper {

    @BaseEntity
    ProjectDTO toProjectDTO( Project project );

    Project toProject( ProjectCreateDTO projectCreateDTO );
}
