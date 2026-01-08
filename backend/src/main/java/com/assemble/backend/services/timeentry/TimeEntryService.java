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
import com.assemble.backend.models.dtos.timeentry.TimeEntryUpdateDTO;
import com.assemble.backend.models.entities.auth.SecurityUser;

import java.util.List;

public interface TimeEntryService {

    List<TimeEntryDTO> getAllTimeEntries();

    TimeEntryDTO getTimeEntryById( String id );

    TimeEntryDTO getOwnTimeEntryById( String id, SecurityUser user );

    List<TimeEntryDTO> getTimeEntriesByEmployeeId( String employeeId );

    List<TimeEntryDTO> getTimeEntriesByProjectId( String projectId );

    List<TimeEntryDTO> getOwnTimeEntries( SecurityUser user );

    TimeEntryDTO createOwnTimeEntry( TimeEntryCreateDTO timeEntryCreateDTO, SecurityUser user );

    TimeEntryDTO createTimeEntry( TimeEntryCreateDTO timeEntryCreateDTO );

    TimeEntryDTO updateTimeEntry( String id, TimeEntryUpdateDTO timeEntryUpdateDTO );

    TimeEntryDTO updateOwnTimeEntry( String id, TimeEntryUpdateDTO timeEntryUpdateDTO, SecurityUser user );

    void deleteTimeEntryById( String id );

    void deleteOwnTimeEntryById( String id, SecurityUser user );
}
