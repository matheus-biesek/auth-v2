package com.mat.auth.domain.port;

import com.mat.auth.domain.model.User;

import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findByUsername(String username);
    void save(User newUser);
    void delete (User user);
}
