package com.busease.service;

import com.busease.model.Bus;
import com.busease.repository.BusRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusService {

    @Autowired
    private BusRepository busRepository;
    
    /**
     * Save a bus
     */
    public Bus saveBus(Bus bus) {
        return busRepository.save(bus);
    }
    
    /**
     * Get a bus by ID
     */
    public Bus getBusById(Long id) {
        Optional<Bus> bus = busRepository.findById(id);
        return bus.orElse(null);
    }
    
    /**
     * Delete a bus by ID
     */
    public void deleteBus(Long id) {
        busRepository.deleteById(id);
    }
    
    /**
     * Get all buses without pagination
     */
    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }
    
    /**
     * Get all buses with pagination
     */
    public Page<Bus> getAllBuses(Pageable pageable) {
        return busRepository.findAll(pageable);
    }
    
    /**
     * Search buses by name, registration number, or type
     */
    public Page<Bus> searchBuses(String query, Pageable pageable) {
        String searchQuery = "%" + query.toLowerCase() + "%";
        return busRepository.findByNameContainingIgnoreCaseOrRegistrationNumberContainingIgnoreCaseOrTypeContainingIgnoreCase(
            searchQuery, searchQuery, searchQuery, pageable);
    }
    
    /**
     * Count all buses
     */
    public long countAllBuses() {
        return busRepository.count();
    }
    
    /**
     * Get active buses
     */
    public List<Bus> getActiveBuses() {
        return busRepository.findByStatus("Active");
    }
}