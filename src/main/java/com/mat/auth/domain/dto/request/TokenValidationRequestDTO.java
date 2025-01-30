package com.mat.auth.domain.dto.request;

import com.mat.auth.domain.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenValidationRequestDTO  {
    private String token;
    private UserRole role;

    public TokenValidationRequestDTO(String token, UserRole role) {
        this.token = token;
        this.role = role;
    }
}
