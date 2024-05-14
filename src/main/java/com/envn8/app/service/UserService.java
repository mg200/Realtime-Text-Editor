package com.envn8.app.service;

import com.envn8.app.models.User;
import com.envn8.app.repository.UserRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User getUserByUsername(String username) {
        // return userRepository.findByUsername(username).orElseThrow(() -> new
        // RuntimeException("User not found"));
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            // Handle the case where the user was not found
            // This could be returning a default value, logging an error, etc.
            System.out.println("User not found");
            return null; // or return a default value
        }
    }

    public void saveUser(User user) {
        userRepository.save(user);

    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // public String forgetPassword(String username) {
    // User user = userRepository.findByUsername(username).orElseThrow(() -> new
    // RuntimeException("User not found"));
    // return "Password reset link sent to your email";
    // }
}
