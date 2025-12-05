package com.assemble.backend.configurations.security;

import com.assemble.backend.dtos.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence( HttpServletRequest request,
                          HttpServletResponse response,
                          AuthenticationException authException
    ) throws IOException {
        response.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
        response.setContentType( MediaType.APPLICATION_JSON_VALUE );
        ErrorResponse responseBody = ErrorResponse.builder()
                .statusCode( HttpStatus.UNAUTHORIZED.value() )
                .statusText( HttpStatus.UNAUTHORIZED.getReasonPhrase() )
                .message( "Not authenticated" )
                .build();
        new ObjectMapper().writeValue( response.getWriter(), responseBody );
    }
}
