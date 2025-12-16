/*
 * assemble
 * SessionServiceTest.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.auth;

import com.assemble.backend.models.dtos.auth.admin.SessionCountDTO;
import com.assemble.backend.models.dtos.auth.admin.SessionDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.MapSession;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SessionService Unit Test")
class SessionServiceTest {

    @Mock
    private FindByIndexNameSessionRepository<MapSession> sessionRepository;

    @InjectMocks
    private SessionServiceImpl sessionService;


    @Test
    @DisplayName("getActiveUserSessionsCount should return 1 sesion")
    void getActiveUserSessionsCount_ShouldReturn1Session_WhenCalled() {
        MapSession session = mock( MapSession.class );
        when( sessionRepository.findByPrincipalName( "admin" ) ).thenReturn( Map.of(
                "session-id", session
        ) );

        SessionCountDTO sessionDTO = assertDoesNotThrow( () -> sessionService.getActiveUserSessionsCount( "admin" ) );

        assertEquals( 1, sessionDTO.getCount() );

        verify( sessionRepository, times( 1 ) ).findByPrincipalName( "admin" );
    }

    @Test
    @DisplayName("getUserSessionDetails should return a list of SessionDTO containing the created session")
    void getUserSessionDetails_ShouldReturnAListOfSessionDTO_WhenCalled() {
        Instant now = Instant.now();
        MapSession session = new MapSession();
        session.setCreationTime( now );
        session.setLastAccessedTime( now );
        session.setCreationTime( now );

        when( sessionRepository.findByPrincipalName( "admin" ) ).thenReturn( Map.of(
                "session-id", session
        ) );

        SessionDTO expected = SessionDTO.builder()
                .id( session.getId() )
                .sessionId( "session-id" )
                .isExpired( false )
                .lastAccessedDate( session.getLastAccessedTime() )
                .createdDate( session.getCreationTime() )
                .principalName( "admin" )
                .build();

        List<SessionDTO> actual = assertDoesNotThrow( () -> sessionService.getUserSessionDetails( "admin" ) );

        assertEquals( List.of( expected ), actual );

        verify( sessionRepository, times( 1 ) ).findByPrincipalName( "admin" );
    }

    @Test
    @DisplayName("invalidateUserSessions should call deleteById on a session key")
    void invalidateUserSessions_ShouldCallDeleteByIdOnASessionKey_WhenCalled() {
        Instant now = Instant.now();
        MapSession session = new MapSession();
        session.setId( "test-id" );
        session.setCreationTime( now );
        session.setLastAccessedTime( now );
        session.setCreationTime( now );

        when( sessionRepository.findByPrincipalName( "admin" ) ).thenReturn( Map.of(
                "session-id", session
        ) );
        assertDoesNotThrow( () -> sessionService.invalidateUserSessions( "admin" ) );


        verify( sessionRepository, times( 1 ) ).findByPrincipalName( "admin" );
        verify( sessionRepository, times( 1 ) ).deleteById( "session-id" );
    }


}