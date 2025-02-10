package com.mat.auth.adapters.rest.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SecurityException extends RuntimeException {

  private final HttpStatus httpStatus;

  public SecurityException(String message, Throwable cause, HttpStatus httpStatus) {
    super(message, cause);
    this.httpStatus = httpStatus;
  }

  public SecurityException(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }

}
