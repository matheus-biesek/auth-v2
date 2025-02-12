package com.mat.auth.unittest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mat.auth.domain.enums.UserRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class TokenGenerationTest extends BaseTokenTest {

    @Test
    void shouldGenerateValidToken() {
        when(user.getUsername()).thenReturn("testuser");
        when(user.getRole()).thenReturn(UserRole.ADMIN);

        String token = tokenService.generateToken(user);

        assertNotNull(token);

        DecodedJWT decodedJWT = JWT.decode(token);
        assertEquals("login-auth-api", decodedJWT.getIssuer());
        assertEquals("testuser", decodedJWT.getSubject());
        assertEquals(UserRole.ADMIN.ordinal(), decodedJWT.getClaim("role").asInt());
        assertNotNull(decodedJWT.getExpiresAt());
    }
}

