/*
 * assemble
 * PasswordMismatchException.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.exceptions.auth;

public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException( String message ) {
        super( message );
    }
}
