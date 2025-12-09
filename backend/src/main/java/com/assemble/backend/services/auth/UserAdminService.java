/*
 * assemble
 * UserAdminService.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.auth;

import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.models.entities.auth.UserRole;

import java.util.List;

public interface UserAdminService {

    List<User> getAllUsers();

    User findUserByUsername( String username );

    User setUserStatus( String username, Boolean enabled );

    User setUserLocked( String username, Boolean locked );

    User setUserRoles( String username, List<UserRole> roles );

    User createUser( User user );

    User updateUser( User user );

    Boolean setPassword( String username, String password );

    Boolean deleteUser( String username );
}
