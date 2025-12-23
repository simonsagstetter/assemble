/*
 * assemble
 * EmployeeEntityListener.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.entities.employee;

import com.assemble.backend.utils.NoGenerator;
import jakarta.persistence.PrePersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class EmployeeEntityListener {

    @Autowired
    @Lazy
    private NoGenerator noGenerator;

    @PrePersist
    public void generateNo( Employee employee ) {
        if ( employee.getNo() == null ) {
            employee.setNo( noGenerator.generateNextNo( "EMPLOYEE_SEQ", "E" ) );
        }
    }
}
