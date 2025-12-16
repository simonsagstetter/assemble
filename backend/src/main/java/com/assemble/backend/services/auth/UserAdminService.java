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

import com.assemble.backend.models.dtos.auth.admin.*;
import com.assemble.backend.models.entities.auth.UserRole;

import java.util.List;

public interface UserAdminService {

    List<UserAdminDTO> getAllUsers();

    UserAdminDTO getUserById( String id );

    List<UserAdminDTO> getUnlinkedUsers();

    UserAdminDTO setUserStatus( String id, UserUpdateStatusDTO userUpdateStatusDTO );

    UserAdminDTO setUserRoles( String username, List<UserRole> roles );

    UserAdminDTO setUserEmployee( String userId, UserUpdateEmployeeDTO userUpdateEmployeeDTO );

    UserAdminDTO createUser( UserCreateDTO user );

    UserAdminDTO updateUser( String id, UserUpdateDTO user );

    void setUserPassword( String id, String newPassword, Boolean invalidateAllSessions );

    void deleteUser( String id );
}
