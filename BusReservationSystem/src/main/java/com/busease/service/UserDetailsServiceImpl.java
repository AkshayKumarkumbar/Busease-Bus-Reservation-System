package com.busease.service;

import com.busease.model.User;
import com.busease.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of Spring Security's UserDetailsService.
 * Loads user-specific data for authentication.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        
        // Create authorities/roles
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // Convert role to Spring Security role (with ROLE_ prefix)
        // Spring Security expects roles to have a ROLE_ prefix
        if (user.getRole() != null) {
            String roleName = user.getRole().startsWith("ROLE_") 
                ? user.getRole() 
                : "ROLE_" + user.getRole();
            authorities.add(new SimpleGrantedAuthority(roleName));
        }
        
        // Return Spring Security User object
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            authorities
        );
    }
}