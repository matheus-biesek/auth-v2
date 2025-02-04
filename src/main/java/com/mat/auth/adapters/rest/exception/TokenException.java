package com.mat.auth.adapters.rest.exception;

public class TokenException extends RuntimeException {
    public TokenException(String message) {
        super(message);
    }

    public TokenException(String message, Exception ex) {
        super(message, ex);
    }
}
