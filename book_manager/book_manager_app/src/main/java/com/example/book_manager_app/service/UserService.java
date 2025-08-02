package com.example.book_manager_app.service;

import com.example.book_manager_app.domain.User;
import com.example.book_manager_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void save(User user) {
        if (user.getId() == null) {
            // New user
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            // Existing user
            User existingUser = userRepository.findById(user.getId()).orElse(null);
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        }
        userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}