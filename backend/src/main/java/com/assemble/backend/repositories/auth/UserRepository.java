package com.assemble.backend.repositories.auth;

import com.assemble.backend.models.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
