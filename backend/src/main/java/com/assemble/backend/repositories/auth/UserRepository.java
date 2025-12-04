package com.assemble.backend.repositories.auth;

import com.assemble.backend.models.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername( String username );
}
