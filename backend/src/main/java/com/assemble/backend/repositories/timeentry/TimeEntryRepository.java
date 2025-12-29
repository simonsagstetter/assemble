/*
 * assemble
 * TimeEntryRepository.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.repositories.timeentry;

import com.assemble.backend.models.entities.timeentry.TimeEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TimeEntryRepository extends JpaRepository<TimeEntry, UUID> {

    List<TimeEntry> findAllByEmployeeId( UUID employeeId );

    List<TimeEntry> findAllByProjectId( UUID projectId );
}
