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
import lombok.NonNull;

import java.util.List;

public interface SessionService {

    void invalidateUserSessions( @NonNull String username );

    SessionCountDTO getActiveUserSessionsCount( @NonNull String username );

    List<SessionDTO> getUserSessionDetails( @NonNull String username );
}
