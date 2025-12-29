/*
 * assemble
 * ProjectEntityListener.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.entities.project;

import com.assemble.backend.utils.NoGenerator;
import jakarta.persistence.PrePersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ProjectEntityListener {

    @Autowired
    @Lazy
    private NoGenerator noGenerator;

    @PrePersist
    public void generateNo( Project project ) {
        if ( project.getNo() == null ) {
            project.setNo( noGenerator.generateNextNo( "PROJECT_SEQ", "P" ) );
        }
    }
}
