package com.assemble.backend.controllers.rest.global;

import com.assemble.backend.dtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionContoller {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public ResponseEntity<?> handleBadCredentialsException() {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode( HttpStatus.UNAUTHORIZED.value() )
                .statusText( HttpStatus.UNAUTHORIZED.getReasonPhrase() )
                .message( "Invalid credentials" )
                .build();
        return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( errorResponse );
    }

}
