package com.busease.repository;

import com.busease.model.Bus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    
    /**
     * Find buses by status
     */
    List<Bus> findByStatus(String status);
    
    /**
     * Search buses by name, registration number, or type (case insensitive)
     */
    Page<Bus> findByNameContainingIgnoreCaseOrRegistrationNumberContainingIgnoreCaseOrTypeContainingIgnoreCase(
        String name, String registrationNumber, String type, Pageable pageable);
}