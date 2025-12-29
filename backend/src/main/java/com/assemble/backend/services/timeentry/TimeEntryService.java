/*
 * assemble
 * TimeEntryService.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.timeentry;

import com.assemble.backend.models.dtos.timeentry.TimeEntryCreateDTO;
import com.assemble.backend.models.dtos.timeentry.TimeEntryDTO;

import java.util.List;

public interface TimeEntryService {

    List<TimeEntryDTO> getAllTimeEntries();

    TimeEntryDTO getTimeEntryById( String id );

    List<TimeEntryDTO> getTimeEntriesByEmployeeId( String employeeId );

    List<TimeEntryDTO> getTimeEntriesByProjectId( String projectId );

    TimeEntryDTO createTimeEntry( TimeEntryCreateDTO timeEntryCreateDTO );

    void deleteTimeEntryById( String id );
}
