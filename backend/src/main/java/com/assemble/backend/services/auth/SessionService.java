/*
 * assemble
 * SessionService.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.auth;

import com.assemble.backend.models.dtos.auth.admin.SessionCountDTO;
import com.assemble.backend.models.dtos.auth.admin.SessionDTO;

import java.util.List;

public interface SessionService {

    void invalidateUserSessions( String username );

    SessionCountDTO getActiveUserSessionsCount( String username );

    List<SessionDTO> getUserSessionDetails( String username );
}
