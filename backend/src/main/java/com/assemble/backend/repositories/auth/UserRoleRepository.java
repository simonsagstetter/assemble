package com.assemble.backend.repositories.auth;

import com.assemble.backend.models.auth.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRoleRepository extends JpaRepository<UserRole, String> {
    Optional<UserRole> findByName( String name );
}
