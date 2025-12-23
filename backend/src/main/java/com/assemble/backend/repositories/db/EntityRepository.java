/*
 * assemble
 * EntityRepository.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.repositories.db;

import com.assemble.backend.models.entities.db.EntityGreeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EntityRepository extends JpaRepository<EntityGreeting, UUID> {
}
