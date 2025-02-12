package com.mat.auth.unittest;

import com.mat.auth.adapters.rest.exception.TokenException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TokenValidationTest extends BaseTokenTest {

    @Test
    void shouldThrowExceptionForExpiredToken() {
        String expiredToken = generateExpiredToken();

        TokenException exception = assertThrows(TokenException.class, () -> {
            tokenService.validateToken(expiredToken);
        });

        assertEquals("Token expirado.", exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getHttpStatus());
    }

    @Test
    void shouldThrowExceptionForMalformedToken() {
        String malformedToken = "malformed-token";

        TokenException exception = assertThrows(TokenException.class, () -> {
            tokenService.validateToken(malformedToken);
        });

        assertEquals("Token malformado ou inválido.", exception.getMessage());
        assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
    }

    @Test
    void shouldThrowExceptionForUnexpectedError() {
        ReflectionTestUtils.setField(tokenService, "secret", null);

        String token = generateExpiredToken();

        TokenException exception = assertThrows(TokenException.class, () -> {
            tokenService.validateToken(token);
        });

        assertEquals("Erro inesperado ao verificar o token.", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
    }
}

