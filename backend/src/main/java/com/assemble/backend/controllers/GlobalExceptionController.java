/*
 * assemble
 * GlobalExceptionController.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.controllers;

import com.assemble.backend.exceptions.auth.PasswordMismatchException;
import com.assemble.backend.models.dtos.global.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.InvalidParameterException;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionController {

    private ResponseEntity<ErrorResponse> createErrorResponse( String message, HttpStatus status ) {
        return ResponseEntity.status( status ).body(
                ErrorResponse.builder()
                        .statusCode( status.value() )
                        .statusText( status.getReasonPhrase() )
                        .message( message )
                        .build()
        );
    }

    @ExceptionHandler(LockedException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleLockedException( LockedException ex ) {
        return createErrorResponse( ex.getMessage(), HttpStatus.LOCKED );
    }

    @ExceptionHandler(DisabledException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleDisabledException( DisabledException ex ) {
        return createErrorResponse( ex.getMessage(), HttpStatus.FORBIDDEN );
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleBadCredentialsException() {
        return createErrorResponse( "Invalid credentials", HttpStatus.UNAUTHORIZED );
    }

    @ExceptionHandler(PasswordMismatchException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handlePasswordMismatchException( PasswordMismatchException ex ) {
        return createErrorResponse( ex.getMessage(), HttpStatus.BAD_REQUEST );
    }

    @ExceptionHandler(InvalidParameterException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleInvalidParameterException( InvalidParameterException ex ) {
        return createErrorResponse( ex.getMessage(), HttpStatus.BAD_REQUEST );
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

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException( EntityNotFoundException ex ) {
        return createErrorResponse( ex.getMessage(), HttpStatus.NOT_FOUND );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException() {
        return createErrorResponse( "A conflict ocurred. Please check your input and try again.", HttpStatus.CONFLICT );
    }

}
