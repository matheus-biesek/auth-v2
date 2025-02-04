package com.mat.auth.adapters.rest.exception;

import com.mat.auth.domain.dto.response.MessageResponseDTO;
import com.mat.auth.domain.exceptions.UserAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        logger.error("Erro inesperado: {}", ex.getMessage(), ex);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Ocorreu um erro interno no servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();

        if (fieldError != null) {
            String errorMessage = fieldError.getDefaultMessage();
            MessageResponseDTO response = new MessageResponseDTO(errorMessage);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        MessageResponseDTO response = new MessageResponseDTO("Erro de validação");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomAuthenticationException.class)
    public ResponseEntity<MessageResponseDTO> handleAuthenticationException(CustomAuthenticationException ex) {
        MessageResponseDTO response = new MessageResponseDTO("Usuário ou senha inválidos");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<MessageResponseDTO> handleTokenException(TokenException ex) {
        logger.error(ex.getMessage(), ex);
        MessageResponseDTO response = new MessageResponseDTO("Ocorreu um erro ao gerar seu token");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<MessageResponseDTO> handleProductExists(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT) // 409
                .body(new MessageResponseDTO(ex.getMessage()));
    }

}
