package com.assemble.backend.repositories.db;

import com.assemble.backend.models.entities.db.EntityGreeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntityRepository extends JpaRepository<EntityGreeting, String> {
}
