package com.mat.auth.domain.port.in;

import com.mat.auth.domain.dto.response.LoginResponseDTO;
import com.mat.auth.domain.enums.UserRole;

public interface SecurityServicePort {
    public LoginResponseDTO login(String username, String password);
    public String registerUserWithRole(String username, String password, UserRole role);
    public void updateRoleUser(String username, UserRole role);
    public void deleteUser(String username);
}
