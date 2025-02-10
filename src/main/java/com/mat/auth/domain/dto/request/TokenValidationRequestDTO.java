package com.mat.auth.domain.dto.request;

import com.mat.auth.domain.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
public class TokenValidationRequestDTO  {
    private String token;
    private UserRole role;
}
