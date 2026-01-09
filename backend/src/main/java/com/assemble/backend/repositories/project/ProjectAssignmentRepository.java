/*
 * assemble
 * ProjectAssignmentRepository.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.repositories.project;

import com.assemble.backend.models.entities.project.ProjectAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectAssignmentRepository extends JpaRepository<ProjectAssignment, UUID> {

    List<ProjectAssignment> findAllByProjectId( UUID projectId );

    List<ProjectAssignment> findAllByEmployeeId( UUID employeeId );

    Optional<ProjectAssignment> findByProject_IdAndEmployee_Id( UUID projectId, UUID employeeId );
}
