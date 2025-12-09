/*
 * assemble
 * UserService.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.auth;

import com.assemble.backend.models.dtos.auth.UserDTO;
import com.assemble.backend.models.entities.auth.SecurityUser;

public interface UserService {

    void changePassword( String username, String oldPassword, String newPassword );

    UserDTO findMe( SecurityUser securityUser );

}
