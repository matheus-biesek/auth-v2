package com.mat.auth.unittest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mat.auth.adapters.rest.exception.TokenException;
import com.mat.auth.domain.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class TokenRoleValidationTest extends BaseTokenTest {

    @Test
    void shouldValidateTokenWithCorrectRole() {
        String token = generateTokenWithRole(UserRole.ADMIN);

        assertTrue(tokenService.validateTokenForRole(token, UserRole.ADMIN));
    }

    @Test
    void shouldThrowExceptionWhenRoleClaimIsAbsent() {
        String token = JWT.create()
                .withIssuer("login-auth-api")
                .withSubject("testuser")
                .withExpiresAt(new Date(System.currentTimeMillis() + 60000))
                .sign(Algorithm.HMAC256(secret));

        TokenException exception = assertThrows(TokenException.class, () -> {
            tokenService.validateTokenForRole(token, UserRole.ADMIN);
        });

        assertEquals("Claim 'role' ausente ou inválida no token", exception.getMessage());
        assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
    }

    @Test
    void shouldThrowExceptionWhenRoleClaimIsInvalid() {
        String token = JWT.create()
                .withIssuer("login-auth-api")
                .withSubject("testuser")
                .withClaim("role", "invalid-role")
                .withExpiresAt(new Date(System.currentTimeMillis() + 60000))
                .sign(Algorithm.HMAC256(secret));

        TokenException exception = assertThrows(TokenException.class, () -> {
            tokenService.validateTokenForRole(token, UserRole.ADMIN);
        });

        assertEquals("Claim 'role' ausente ou inválida no token", exception.getMessage());
        assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
    }

    @Test
    void shouldThrowExceptionWhenRoleDoesNotMatch() {
        String token = generateTokenWithRole(UserRole.USER);

        TokenException exception = assertThrows(TokenException.class, () -> {
            tokenService.validateTokenForRole(token, UserRole.ADMIN);
        });

        assertEquals("Papel do token inválido", exception.getMessage());
        assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
    }
}
