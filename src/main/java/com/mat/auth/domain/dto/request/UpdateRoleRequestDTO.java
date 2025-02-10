package com.mat.auth.domain.dto.request;

import com.mat.auth.domain.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
public class UpdateRoleRequestDTO {

    @NotBlank(message = "O nome de usuário não pode ser nulo, vazio ou em branco!")
    String username;

    @NotNull(message = "O papel de usuário não pode ser nulo!")
    UserRole role;
}
