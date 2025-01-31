package com.mat.auth.domain.port.in;

import com.mat.auth.domain.enums.UserRole;

import java.util.Optional;

public interface SecurityServicePort {
    public Optional<String> login(String username, String password);
    public String registerUserWithRole(String username, String password, UserRole role);
    public void updateRoleUser(String username, UserRole role);
    public void deleteUser(String username);
}
