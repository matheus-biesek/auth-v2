package com.mat.auth.domain.port.in;

import com.mat.auth.domain.dto.response.*;
import com.mat.auth.domain.enums.UserRole;

public interface SecurityServicePort {
    public TokenResponseDTO login(String username, String password);
    public TokenResponseDTO registerUserWithRole(String username, String password, UserRole role);
    public SuccessResponseDTO updateRoleUser(String username, UserRole role);
    public SuccessResponseDTO deleteUser(String username);
}
