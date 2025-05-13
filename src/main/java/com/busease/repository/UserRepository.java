package com.busease.repository;

import com.busease.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by username
     */
    User findByUsername(String username);
    
    /**
     * Find user by email
     */
    User findByEmail(String email);
    
    /**
     * Find users by role
     */
    java.util.List<User> findByRole(String role);
}