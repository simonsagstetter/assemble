/*
 * assemble
 * SessionServiceImpl.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.auth;

import com.assemble.backend.models.dtos.auth.admin.SessionCountDTO;
import com.assemble.backend.models.dtos.auth.admin.SessionDTO;
import lombok.AllArgsConstructor;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    @Override
    @Transactional
    public void invalidateUserSessions( String username ) {
        Map<String, ? extends Session> sessions = sessionRepository.findByPrincipalName( username );
        sessions.keySet().forEach( sessionRepository::deleteById );
    }

    @Override
    public SessionCountDTO getActiveUserSessionsCount( String username ) {
        long count = sessionRepository.findByPrincipalName( username )
                .values().stream()
                .filter( session -> !session.isExpired() )
                .count();

        return SessionCountDTO.builder().count( count ).build();
    }

    @Override
    public List<SessionDTO> getUserSessionDetails( String username ) {
        Map<String, ? extends Session> sessions = sessionRepository.findByPrincipalName( username );
        return sessions.entrySet().stream()
                .map( stringEntry ->
                        SessionDTO.builder()
                                .id( stringEntry.getValue().getId() )
                                .sessionId( stringEntry.getKey() )
                                .principalName( username )
                                .createdDate( stringEntry.getValue().getCreationTime() )
                                .isExpired( stringEntry.getValue().isExpired() )
                                .lastAccessedDate( stringEntry.getValue().getLastAccessedTime() )
                                .build()
                )
                .toList();
    }
}
