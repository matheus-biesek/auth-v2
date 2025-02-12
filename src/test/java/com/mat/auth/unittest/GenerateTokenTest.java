package com.mat.auth.unittest;

import com.mat.auth.adapters.rest.exception.TokenException;
import com.mat.auth.application.service.TokenServiceImpl;
import com.mat.auth.domain.enums.UserRole;
import com.mat.auth.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
public class GenerateTokenTest {

    @InjectMocks
    private TokenServiceImpl tokenService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        String secret = "test-secret";
        ReflectionTestUtils.setField(tokenService, "secret", secret);
    }

    @Test
    void testGenerateTokenSuccess() {
        User user = new User("username", UserRole.USER);
        String token = tokenService.generateToken(user);

        assertNotNull(token);
        assertTrue(token.startsWith("eyJ"));
    }

    @Test
    void testValidateToken_MalformedOrInvalidToken() {
        String invalidToken = "invalid.token.value";

        TokenException exception = assertThrows(TokenException.class, () -> {
            tokenService.validateToken(invalidToken);
        });

        assertEquals("Token malformado ou inválido.", exception.getMessage());
    }

}
