/*
 * assemble
 * ProjectAssignmentService.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.project;

import com.assemble.backend.models.dtos.project.ProjectAssignmentCreateDTO;
import com.assemble.backend.models.dtos.project.ProjectAssignmentDTO;
import com.assemble.backend.models.entities.auth.SecurityUser;

import java.util.List;

public interface ProjectAssignmentService {

    ProjectAssignmentDTO getProjectAssignmentById( String id );

    List<ProjectAssignmentDTO> getOwnProjectAssignments( SecurityUser user );

    List<ProjectAssignmentDTO> getProjectAssignmentsByProjectId( String projectId );

    List<ProjectAssignmentDTO> getProjectAssignmentsByEmployeeId( String employeeId );

    ProjectAssignmentDTO createProjectAssignment( ProjectAssignmentCreateDTO projectAssignmentCreateDTO );

    void deleteProjectAssignmentById( String id );
}
