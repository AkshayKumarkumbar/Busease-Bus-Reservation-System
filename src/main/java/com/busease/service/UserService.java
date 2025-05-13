package com.busease.service;

import com.busease.model.User;
import com.busease.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Find a user by username - required for simple login
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * Check if a password matches the stored hash - for simple login
     */
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
    
    /**
     * Register a new user
     */
    public User registerUser(User user) {
        // Check if username or email already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }
        
        // Encrypt the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Set default role if not provided
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }
        
        return userRepository.save(user);
    }
    
    /**
     * Create a new user (alias for registerUser)
     */
    public User createUser(User user) {
        return registerUser(user);
    }
    
    /**
     * Get a user by ID
     */
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }
    
    /**
     * Get a user by username
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * Get a user by email
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Check if email exists
     */
    public boolean isEmailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
    
    /**
     * Check if username exists
     */
    public boolean isUsernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }
    
    /**
     * Update a user
     */
    public User updateUser(User user) {
        return userRepository.save(user);
    }
    
    /**
     * Delete a user by ID
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Count all users
     */
    public long countAllUsers() {
        return userRepository.count();
    }
    
    /**
     * Get all users with pagination
     */
    public Page<User> getAllUsers(org.springframework.data.domain.Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}