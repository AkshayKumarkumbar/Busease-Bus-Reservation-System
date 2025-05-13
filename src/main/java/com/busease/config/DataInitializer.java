package com.busease.config;

import com.busease.model.Bus;
import com.busease.model.Route;
import com.busease.model.Seat;
import com.busease.model.User;
import com.busease.repository.BusRepository;
import com.busease.repository.RouteRepository;
import com.busease.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * This class initializes sample data for testing purposes.
 * Simplified to resolve dependency issues.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private RouteRepository routeRepository;
    
    @Autowired(required = false)
    private UserRepository userRepository;
    
    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Initializing sample data...");
        
        // Create admin user in the database
        if (userRepository != null && passwordEncoder != null) {
            if (!userExists("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@busease.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("ADMIN");
                userRepository.save(admin);
                logger.info("Admin user created successfully");
            } else {
                logger.info("Admin user already exists");
            }
        } else {
            logger.warn("UserRepository or PasswordEncoder is null, skipping admin user creation");
        }
        
        // Check if we already have buses
        if (busRepository.count() == 0) {
            initializeSampleData();
        } else {
            logger.info("Sample data already exists, skipping initialization");
        }
    }
    
    private boolean userExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    private void initializeSampleData() {
        try {
            // Create sample buses
            Bus luxuryBus = new Bus("Luxury Express", "MH-01-AA-1234", "AC Sleeper", 40, "Active");
            
            // Add seats to buses
            for (int i = 1; i <= luxuryBus.getCapacity(); i++) {
                Seat seat = new Seat();
                seat.setSeatNumber(String.valueOf(i));
                seat.setStatus("Available");
                luxuryBus.addSeat(seat);
            }
            
            // Save buses
            luxuryBus = busRepository.save(luxuryBus);
            logger.info("Created sample bus: {}", luxuryBus);
            
            // Create sample routes
            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);
    
            // Mumbai to Pune route
            Route mumbaiToPune = new Route(
                "MP" + today.getYear() + today.getMonthValue() + today.getDayOfMonth() + "001",
                "Mumbai", 
                "Pune", 
                today, 
                LocalTime.of(10, 0), 
                today, 
                LocalTime.of(13, 0), 
                500.0, 
                40,
                luxuryBus
            );
            
            // Pune to Mumbai route
            Route puneToMumbai = new Route(
                "PM" + today.getYear() + today.getMonthValue() + today.getDayOfMonth() + "001",
                "Pune", 
                "Mumbai", 
                tomorrow, 
                LocalTime.of(16, 0), 
                tomorrow, 
                LocalTime.of(19, 0), 
                500.0, 
                40,
                luxuryBus
            );
            
            // Save routes
            mumbaiToPune = routeRepository.save(mumbaiToPune);
            puneToMumbai = routeRepository.save(puneToMumbai);
            
            logger.info("Created sample route: {}", mumbaiToPune);
            logger.info("Created sample route: {}", puneToMumbai);
        } catch (Exception e) {
            logger.error("Error initializing sample data: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}