package com.mat.auth.application.service;

import com.mat.auth.adapters.persistence.UserRepositoryAdapter;
import com.mat.auth.adapters.rest.exception.LoginException;
import com.mat.auth.domain.dto.response.LoginResponseDTO;
import com.mat.auth.domain.exceptions.UserAlreadyExistsException;
import com.mat.auth.domain.exceptions.UserNotFoundException;
import com.mat.auth.domain.port.in.SecurityServicePort;
import com.mat.auth.domain.enums.UserRole;
import com.mat.auth.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityServicePort {

    private final UserRepositoryAdapter userRepositoryAdapter;
    private final PasswordEncoder passwordEncoder;
    private final TokenServiceImpl tokenServiceImpl;

    private User findUserByUsername(String username, RuntimeException exception) {
        return userRepositoryAdapter.findByUsername(username)
                .orElseThrow(() -> exception);
    }

    public LoginResponseDTO login(String username, String password) {
        User user = findUserByUsername(username, new LoginException("Usuário não encontrado", HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new LoginException("Senha inválida", HttpStatus.UNAUTHORIZED);
        }

        String token = tokenServiceImpl.generateToken(user);
        return new LoginResponseDTO(token);
    }





    @Override
    public String registerUserWithRole(String username, String password, UserRole role) {
        if (userRepositoryAdapter.findByUsername(username).isPresent()) {
            // criar console global
            throw new UserAlreadyExistsException("O usuário já existe!");
        }
        User newUser = new User(passwordEncoder.encode(password), username, role);
        userRepositoryAdapter.save(newUser);
        return tokenServiceImpl.generateToken(newUser);
    }

    @Override
    public void updateRoleUser(String username, UserRole role) {
        User user = userRepositoryAdapter.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado!"));
        user.setRole(role);
        userRepositoryAdapter.save(user);
    }

    @Override
    public void deleteUser(String username) {
        User user = userRepositoryAdapter.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado!"));
        userRepositoryAdapter.delete(user);
    }
}
