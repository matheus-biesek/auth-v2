package com.mat.auth.adapters.rest.exception;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.mat.auth.domain.dto.response.ErrorResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // REQUEST
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(new ErrorResponseDTO("O corpo da requisição é obrigatório!"), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        logger.error("Método HTTP não suportado: {}", ex.getMethod(), ex);

        String mensagem = "O método HTTP " + ex.getMethod() + " não é suportado para este endpoint. Métodos permitidos: " + ex.getSupportedHttpMethods();

        return new ResponseEntity<>(new ErrorResponseDTO(mensagem), HttpStatus.METHOD_NOT_ALLOWED);
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

    // DATA BASE
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDatabaseException(DataIntegrityViolationException ex) {
        logger.warn("Erro de integridade no banco de dados: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(new ErrorResponseDTO("Erro de integridade no banco de dados."), HttpStatus.BAD_REQUEST);
    }

    // TOKEN
    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ErrorResponseDTO> handleTokenException(TokenException ex) {
        logger.error("Ocorreu um erro ao precessar token: {}", ex.getMessage(), ex);

        return new ResponseEntity<>(new ErrorResponseDTO(ex.getMessage()), ex.getHttpStatus());
    }
    @ExceptionHandler(JWTCreationException.class)
    public ResponseEntity<ErrorResponseDTO> handleJWTCreationException(JWTCreationException ex) {
        logger.error("Erro ao criar JWT: {}", ex.getMessage(), ex);

        return new ResponseEntity<>(new ErrorResponseDTO("Erro ao gerar token de autenticação. Tente novamente."), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // AUTH
    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ErrorResponseDTO> handleLoginException(LoginException ex) {
        logger.error(ex.getMessage(), ex);

        return new ResponseEntity<>(new ErrorResponseDTO("Usuário ou senha inválido"), ex.getHttpStatus());
    }
    @ExceptionHandler(registerUserWithRoleException.class)
    public ResponseEntity<ErrorResponseDTO> handleLoginException(registerUserWithRoleException ex) {
        logger.error(ex.getMessage(), ex);

        return new ResponseEntity<>(new ErrorResponseDTO("Não foi possível registrar o usuário. Tente novamente com outro nome de usuário."), ex.getHttpStatus());
    }
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorResponseDTO> handleLRegisterUserWithRoleException(SecurityException ex) {
        logger.error("Ocorreu um erro de autentificação: {}", ex.getMessage(), ex);

        return new ResponseEntity<>(new ErrorResponseDTO(ex.getMessage()), ex.getHttpStatus());
    }

    // ALL
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        logger.error("Erro inesperado: {}", ex.getMessage(), ex);

        return new ResponseEntity<>(new ErrorResponseDTO("Ocorreu um erro interno no servidor!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
