package com.mat.auth.unittest;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.mat.auth.adapters.rest.exception.TokenException;
import com.mat.auth.application.service.TokenServiceImpl;
import com.mat.auth.domain.enums.UserRole;
import com.mat.auth.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class GenerateTokenTest {
    @InjectMocks
    private TokenServiceImpl tokenService;

    @Mock
    private DecodedJWT decodedJWT;

    private final String secret = "test-secret";
    private final String validToken = "eyJfakeValidToken";
    private final String expiredToken = "eyJfakeExpiredToken";
    private final String malformedToken = "invalidToken";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(tokenService, "secret", secret);
    }


    @Test
    void testGenerateTokenSuccess() {
        User user = new User("username", UserRole.USER);
        String token = tokenService.generateToken(user);

        assertNotNull(token);
        assertTrue(token.startsWith("eyJ"));
    }


}
