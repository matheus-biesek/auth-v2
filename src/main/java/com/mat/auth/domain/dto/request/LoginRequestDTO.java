package com.mat.auth.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequestDTO {

    @NotBlank(message = "O nome de usuário não pode ser nulo, vazio ou em branco!")
    private String username;

    @NotBlank(message = "A senha não pode ser nula, vazia ou em branco!")
    private String password;
}
