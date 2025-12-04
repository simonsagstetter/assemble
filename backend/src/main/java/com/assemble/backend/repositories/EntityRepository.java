package com.assemble.backend.repositories;

import com.assemble.backend.models.db.EntityGreeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityRepository extends JpaRepository<EntityGreeting, String> {
}
