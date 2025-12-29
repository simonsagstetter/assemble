/*
 * assemble
 * TimeEntryEntityListener.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.entities.timeentry;

import com.assemble.backend.utils.NoGenerator;
import jakarta.persistence.PrePersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class TimeEntryEntityListener {

    @Autowired
    @Lazy
    private NoGenerator noGenerator;

    @PrePersist
    public void generateNo( TimeEntry timeEntry ) {
        if ( timeEntry.getNo() == null ) {
            timeEntry.setNo( noGenerator.generateNextNo( "TIME_ENTRY_SEQ", "T" ) );
        }
    }
}
