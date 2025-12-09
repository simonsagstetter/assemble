package com.assemble.backend.configurations.security;

import com.assemble.backend.models.dtos.global.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle( HttpServletRequest request,
                        HttpServletResponse response,
                        AccessDeniedException accessDeniedException
    ) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String message = authentication.isAuthenticated() ? "Already authenticated" : "Not authenticated";
        response.setStatus( HttpServletResponse.SC_FORBIDDEN );
        response.setContentType( MediaType.APPLICATION_JSON_VALUE );
        ErrorResponse responseBody = ErrorResponse.builder()
                .statusCode( HttpStatus.FORBIDDEN.value() )
                .statusText( HttpStatus.FORBIDDEN.getReasonPhrase() )
                .message( message )
                .build();
        new ObjectMapper().writeValue( response.getWriter(), responseBody );
    }
}
