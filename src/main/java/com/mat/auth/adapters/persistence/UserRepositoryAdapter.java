package com.mat.auth.adapters.persistence;

import com.mat.auth.domain.port.out.UserRepositoryPort;
import com.mat.auth.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username);
    }

    @Override
    public void save(User newUser) { userJpaRepository.save(newUser); }

    @Override
    public void delete(User user) { userJpaRepository.delete(user); }
}
