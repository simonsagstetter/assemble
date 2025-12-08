package com.assemble.backend.controllers.rest.global;

import com.assemble.backend.dtos.global.ErrorResponse;
import com.assemble.backend.dtos.global.FieldValidationError;
import com.assemble.backend.dtos.global.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleBadCredentialsException() {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode( HttpStatus.UNAUTHORIZED.value() )
                .statusText( HttpStatus.UNAUTHORIZED.getReasonPhrase() )
                .message( "Invalid credentials" )
                .build();
        return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( errorResponse );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException( MethodArgumentNotValidException ex ) {
        List<FieldValidationError> errors = ex.getFieldErrors().stream()
                .map( fieldError -> FieldValidationError.builder()
                        .errorMessage( fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "Invalid value" )
                        .fieldName( fieldError.getField() )
                        .build() )
                .toList();

        ValidationErrorResponse body = ValidationErrorResponse.builder()
                .message( "Validation failed" )
                .statusCode( HttpStatus.BAD_REQUEST.value() )
                .statusText( HttpStatus.BAD_REQUEST.getReasonPhrase() )
                .errors( errors )
                .build();

        return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( body );
    }

}
