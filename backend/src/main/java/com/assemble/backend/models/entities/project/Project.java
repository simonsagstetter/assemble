/*
 * assemble
 * Project.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.entities.project;

import com.assemble.backend.models.entities.core.BaseJPAEntity;
import com.assemble.backend.models.entities.timeentry.TimeEntry;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(ProjectEntityListener.class)
@Entity
@Table(name = "projects")
public class Project extends BaseJPAEntity {

    @Column(name = "NO", unique = true)
    private String no;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Builder.Default
    @Column(name = "ACTIVE", nullable = false)
    private boolean active = true;

    @Enumerated(value = EnumType.STRING)
    private ProjectType type;

    @Enumerated(value = EnumType.STRING)
    private ProjectStage stage;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(mappedBy = "project")
    @Builder.Default
    private Set<TimeEntry> timeEntries = new HashSet<>();

    @Column(name = "COLOR", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectColor color;

}
