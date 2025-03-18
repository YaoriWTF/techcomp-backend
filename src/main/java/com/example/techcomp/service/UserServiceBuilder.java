package com.example.techcomp.service;

import com.example.techcomp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceBuilder {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceBuilder setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
        return this;
    }

    public UserServiceBuilder setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        return this;
    }

    public UserService createUserService() {
        // Передаем все необходимые параметры в конструктор UserService
        return new UserService(userRepository, passwordEncoder);
    }
}