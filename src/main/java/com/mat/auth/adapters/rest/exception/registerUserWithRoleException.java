package com.mat.auth.adapters.rest.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class registerUserWithRoleException extends RuntimeException {

    private final HttpStatus httpStatus;

    public registerUserWithRoleException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public registerUserWithRoleException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
