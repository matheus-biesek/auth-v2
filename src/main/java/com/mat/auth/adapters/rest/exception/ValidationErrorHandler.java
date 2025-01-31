package com.mat.auth.adapters.rest.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

public class ValidationErrorHandler {

    public static String getErrorMessages(BindingResult result) {
        if (result.hasErrors()) {
            return result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
        }
        return null;
    }
}
