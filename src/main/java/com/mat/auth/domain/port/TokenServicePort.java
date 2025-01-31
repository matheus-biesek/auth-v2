package com.mat.auth.domain.port;

import com.mat.auth.domain.enums.UserRole;
import com.mat.auth.domain.model.User;

public interface TokenServicePort {
    String generateToken(User user);
    String validateToken(String token);
    boolean validateTokenForRole(String token, UserRole role);
}
