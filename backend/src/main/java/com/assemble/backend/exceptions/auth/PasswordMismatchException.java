package com.assemble.backend.exceptions.auth;

public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException( String message ) {
        super( message );
    }
}
