package com.busease;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Main entry point for the Busease Bus Reservation System application.
 * This class starts the Spring Boot application.
 * 
 * Application is configured to:
 * - Use port 5000 in Replit environment (use PORT env variable)
 * - Use port 8080 in local/IDE environments (default, defined in application.properties)
 * - Support both H2 in-memory DB and MySQL for flexibility
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.busease")
@EntityScan("com.busease.model")
@EnableJpaRepositories("com.busease.repository")
public class BuseaseApplication {

    public static void main(String[] args) {
        // For Replit environment
        if (System.getenv("REPL_ID") != null && System.getenv("REPL_OWNER") != null) {
            System.setProperty("PORT", "5000");
            System.setProperty("spring.main.allow-bean-definition-overriding", "true");
        }
        
        // Launch the application
        SpringApplication.run(BuseaseApplication.class, args);
    }
    
    /**
     * Very simplified security configuration - just open all routes
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/**").permitAll()
                .and()
            .formLogin().disable() // Disable Spring Security's form login
            .logout().disable()    // Disable Spring Security's logout
            .headers().frameOptions().disable(); // Enable H2 console
        
        return http.build();
    }
    
    /**
     * Simple password encoder for basic password handling
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
