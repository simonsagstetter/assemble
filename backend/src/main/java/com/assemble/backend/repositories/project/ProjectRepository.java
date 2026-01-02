/*
 * assemble
 * ProjectRepository.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.repositories.project;


import com.assemble.backend.models.entities.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    @Query("""
                SELECT p
                FROM Project p
                WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            """)
    List<Project> searchAll( @Param("searchTerm") String searchTerm );
}
