/*
 * assemble
 * ProjectAssignment.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.entities.project;

import com.assemble.backend.models.entities.core.BaseJPAEntity;
import com.assemble.backend.models.entities.employee.Employee;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "project_assignments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "employee_id", "project_id" })
        }
)
public class ProjectAssignment extends BaseJPAEntity {

    @ManyToOne(optional = false)
    private Employee employee;

    @ManyToOne(optional = false)
    private Project project;

    @Builder.Default
    @Column(name = "ACTIVE", nullable = false)
    private boolean active = true;

    @Column(name = "HOURLY_RATE")
    private BigDecimal hourlyRate;
}
