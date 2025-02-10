package com.mat.auth.application.service;

import com.mat.auth.adapters.persistence.UserRepositoryAdapter;
import com.mat.auth.adapters.rest.exception.LoginException;
import com.mat.auth.adapters.rest.exception.SecurityException;
import com.mat.auth.adapters.rest.exception.registerUserWithRoleException;
import com.mat.auth.domain.dto.response.*;
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

    public TokenResponseDTO login(String username, String password) {
        User user = findUserByUsername(username, new LoginException("Usuário não encontrado.", HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new LoginException("Senha inválida.", HttpStatus.UNAUTHORIZED);
        }

        String token = tokenServiceImpl.generateToken(user);
        return new TokenResponseDTO(token);
    }

    @Override
    public TokenResponseDTO registerUserWithRole(String username, String password, UserRole role) {
        if (userRepositoryAdapter.findByUsername(username).isPresent()) {
            throw new registerUserWithRoleException("Usuário já existe no banco de dados!", HttpStatus.CONFLICT);
        }

        User newUser = new User(passwordEncoder.encode(password), username, role);
        userRepositoryAdapter.save(newUser);

        String token = tokenServiceImpl.generateToken(newUser);
        return new TokenResponseDTO(token);
    }

    @Override
    public SuccessResponseDTO updateRoleUser(String username, UserRole role) {
        User user = findUserByUsername(username, new SecurityException("Usuário não encontrado.", HttpStatus.NOT_FOUND));

        user.setRole(role);
        userRepositoryAdapter.save(user);

        return new SuccessResponseDTO("Role do usuário trocada com sucesso.");
    }

    @Override
    public SuccessResponseDTO deleteUser(String username) {
        User user = findUserByUsername(username, new SecurityException("Usuário não encontrado!", HttpStatus.NOT_FOUND));

        userRepositoryAdapter.delete(user);

        return new SuccessResponseDTO("Usuário deletado com sucesso.");
    }
}
