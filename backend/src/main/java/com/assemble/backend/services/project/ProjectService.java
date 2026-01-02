/*
 * assemble
 * ProjectService.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.project;

import com.assemble.backend.models.dtos.project.ProjectCreateDTO;
import com.assemble.backend.models.dtos.project.ProjectDTO;

import java.util.List;

public interface ProjectService {

    List<ProjectDTO> getAllProjects();

    ProjectDTO getProjectById( String id );

    List<ProjectDTO> searchAllProjects( String searchTerm );

    ProjectDTO createProject( ProjectCreateDTO projectCreateDTO );

    void deleteProjectById( String id );
}
