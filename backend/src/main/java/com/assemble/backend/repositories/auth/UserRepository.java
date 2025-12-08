package com.assemble.backend.repositories.auth;

import com.assemble.backend.models.auth.User;
import com.assemble.backend.models.auth.UserRole;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername( String username );

    long countDistinctByRolesContaining( @NonNull UserRole role );
}
