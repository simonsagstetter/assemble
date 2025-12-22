/*
 * assemble
 * UserRepository.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.repositories.auth;

import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.models.entities.auth.UserRole;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername( String username );

    long countDistinctByRolesContaining( @NonNull UserRole role );

    @Query("""
                SELECT u
                FROM User u
                WHERE u.employee IS NULL
                AND LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            """)
    List<User> search( @Param("searchTerm") String searchTerm );
}
