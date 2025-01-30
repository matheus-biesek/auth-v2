package com.mat.auth.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterUserRequestDTO {

    @NotBlank(message = "O nome de usuário não pode ser nulo, vazio ou em branco!")
    @Size(min = 3, max = 15, message = "O nome de usuário deve ter entre 3 e 15 caracteres!")
    private String username;

    @NotBlank(message = "A senha não pode ser nula, vazia ou em branco!")
    @Size(min = 6, max = 15, message = "A senha deve ter entre 6 e 15 caracteres!")
    private String password;
}
