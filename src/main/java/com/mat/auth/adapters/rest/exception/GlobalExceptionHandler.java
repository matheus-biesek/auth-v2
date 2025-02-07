package com.mat.auth.adapters.rest.exception;

import com.mat.auth.domain.dto.response.ErrorResponseDTO;
import com.mat.auth.domain.exceptions.UserAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        logger.error("Erro inesperado: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(new ErrorResponseDTO("Ocorreu um erro interno no servidor!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(new ErrorResponseDTO("O corpo da requisição é obrigatório!"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();

        if (fieldError != null) {
            String errorMessage = fieldError.getDefaultMessage();
            return new ResponseEntity<>(new ErrorResponseDTO(errorMessage), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ErrorResponseDTO("Erro de validação!"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ErrorResponseDTO> handleTokenException(TokenException ex) {
        logger.error(ex.getMessage(), ex);

        String mensagem = switch (ex.getHttpStatus()) {
            case UNAUTHORIZED -> "Token expirado ou inválido.";
            case FORBIDDEN -> "Acesso negado para esse token.";
            default -> "Ocorreu um erro ao processar o token.";
        };

        return new ResponseEntity<>(new ErrorResponseDTO(mensagem), ex.getHttpStatus());
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ErrorResponseDTO> handleLoginException(LoginException ex) {
        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new ErrorResponseDTO("Usuário ou senha inválido"), ex.getHttpStatus());
    }

}
