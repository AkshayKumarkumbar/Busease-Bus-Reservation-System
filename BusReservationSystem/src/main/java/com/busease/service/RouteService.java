package com.busease.service;

import com.busease.model.Route;
import com.busease.repository.RouteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;
    
    /**
     * Save a route
     */
    public Route saveRoute(Route route) {
        return routeRepository.save(route);
    }
    
    /**
     * Get a route by ID
     */
    public Route getRouteById(Long id) {
        Optional<Route> route = routeRepository.findById(id);
        return route.orElse(null);
    }
    
    /**
     * Delete a route by ID
     */
    public void deleteRoute(Long id) {
        routeRepository.deleteById(id);
    }
    
    /**
     * Get all routes without pagination
     */
    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }
    
    /**
     * Get all routes with pagination
     */
    public Page<Route> getAllRoutes(Pageable pageable) {
        return routeRepository.findAll(pageable);
    }
    
    /**
     * Search routes by origin, destination, or route code
     */
    public Page<Route> searchRoutes(String query, Pageable pageable) {
        String searchQuery = "%" + query.toLowerCase() + "%";
        return routeRepository.findByOriginContainingIgnoreCaseOrDestinationContainingIgnoreCaseOrRouteCodeContainingIgnoreCase(
            searchQuery, searchQuery, searchQuery, pageable);
    }
    
    /**
     * Count all routes
     */
    public long countAllRoutes() {
        return routeRepository.count();
    }
    
    /**
     * Search routes by origin, destination and date
     */
    public List<Route> searchRoutes(String origin, String destination, LocalDate departureDate) {
        return routeRepository.findByOriginContainingIgnoreCaseAndDestinationContainingIgnoreCaseAndDepartureDate(
            origin, destination, departureDate);
    }
    
    /**
     * Find routes by origin, destination and date (exact match)
     */
    public List<Route> findRoutesByOriginDestinationAndDate(String origin, String destination, LocalDate departureDate) {
        return routeRepository.findByOriginIgnoreCaseAndDestinationIgnoreCaseAndDepartureDate(
            origin, destination, departureDate);
    }
    
    /**
     * Find routes by origin and destination only
     */
    public List<Route> findRoutesByOriginAndDestination(String origin, String destination) {
        return routeRepository.findByOriginIgnoreCaseAndDestinationIgnoreCase(origin, destination);
    }
    
    /**
     * Find routes by departure date only
     */
    public List<Route> findRoutesByDepartureDate(LocalDate departureDate) {
        return routeRepository.findByDepartureDate(departureDate);
    }
}