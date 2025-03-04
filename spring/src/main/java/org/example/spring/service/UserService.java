package org.example.spring.service;

import org.example.spring.model.User;
import org.example.spring.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean verifyPassword(String username, String rawPassword) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(value -> passwordEncoder.matches(rawPassword, value.getPassword())).orElse(false);
    }

    public User registerUser(String username, String password) {
        if (userExists(username)) {
            throw new IllegalStateException("User already exists");
        }
        String hashedPassword = passwordEncoder.encode(password);
        User newUser = new User(username, hashedPassword);
        return userRepository.save(newUser);
    }
}
