/*
 * assemble
 * TimeEntry.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.entities.timeentry;

import com.assemble.backend.models.entities.core.BaseJPAEntity;
import com.assemble.backend.models.entities.employee.Employee;
import com.assemble.backend.models.entities.project.Project;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.time.DurationMax;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(TimeEntryEntityListener.class)
@Entity
@Table(name = "time_entries")
public class TimeEntry extends BaseJPAEntity {

    @Column(name = "NO", unique = true)
    private String no;

    @ManyToOne(optional = false)
    private Employee employee;

    @ManyToOne(optional = false)
    private Project project;

    @NonNull
    @Column(name = "DESCRIPTION", nullable = false, length = 1000)
    private String description;

    @Column(name = "START_TIME")
    private Instant startTime;

    @Column(name = "END_TIME")
    private Instant endTime;

    @Column(name = "PAUSE_TIME")
    @DurationMax(hours = 23, minutes = 59, seconds = 59)
    private Duration pauseTime;

    @Column(name = "TOTAL_TIME")
    @DurationMax(hours = 23, minutes = 59, seconds = 59)
    private Duration totalTime;

    @NonNull
    @Column(name = "RATE", nullable = false)
    private BigDecimal rate;

    @NonNull
    @Column(name = "TOTAL", nullable = false)
    private BigDecimal total;

    @NonNull
    @Column(name = "TOTAL_INTERNAL", nullable = false)
    private BigDecimal totalInternal;
}
