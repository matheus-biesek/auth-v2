package com.mat.auth.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsernameRequestDTO {

    @NotBlank(message = "O nome de usuário não pode ser nulo, vazio ou em branco!")
    @Size(min = 3, max = 15, message = "O nome de usuário deve ter entre 3 e 15 caracteres!")
    private String username;
}
