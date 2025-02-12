package com.mat.auth.unittest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mat.auth.application.service.TokenServiceImpl;
import com.mat.auth.domain.enums.UserRole;
import com.mat.auth.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

public abstract class BaseTokenTest {

    @InjectMocks
    protected TokenServiceImpl tokenService;

    @Mock
    protected User user;

    protected String secret;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        secret = "my-secret-key";
        ReflectionTestUtils.setField(tokenService, "secret", secret);
    }

    protected String generateExpiredToken() {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer("login-auth-api")
                .withSubject("testuser")
                .withClaim("role", UserRole.USER.ordinal())
                .withExpiresAt(new Date(System.currentTimeMillis() - 1000))
                .sign(algorithm);
    }

    protected String generateTokenWithRole(UserRole role) {
        return JWT.create()
                .withIssuer("login-auth-api")
                .withSubject("testuser")
                .withClaim("role", role.ordinal())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60000))
                .sign(Algorithm.HMAC256(secret));
    }
}
