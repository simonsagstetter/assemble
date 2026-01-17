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
import com.assemble.backend.models.dtos.project.ProjectRefDTO;
import com.assemble.backend.models.dtos.project.ProjectUpdateDTO;
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

    ProjectRefDTO toProjectRefDTO( Project project );

    Project toProject( ProjectCreateDTO projectCreateDTO );

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "name", source = "projectUpdateDTO.name")
    @Mapping(target = "description", source = "projectUpdateDTO.description")
    @Mapping(target = "active", source = "projectUpdateDTO.active")
    @Mapping(target = "type", source = "projectUpdateDTO.type")
    @Mapping(target = "stage", source = "projectUpdateDTO.stage")
    @Mapping(target = "category", source = "projectUpdateDTO.category")
    @Mapping(target = "color", source = "projectUpdateDTO.color")
    Project toProject( ProjectUpdateDTO projectUpdateDTO, @MappingTarget Project project );
}
