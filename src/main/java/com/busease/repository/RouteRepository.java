package com.busease.repository;

import com.busease.model.Route;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    
    /**
     * Find routes by origin or destination or route code (case insensitive)
     */
    Page<Route> findByOriginContainingIgnoreCaseOrDestinationContainingIgnoreCaseOrRouteCodeContainingIgnoreCase(
        String origin, String destination, String routeCode, Pageable pageable);
    
    /**
     * Find routes by origin and destination and departure date
     */
    List<Route> findByOriginContainingIgnoreCaseAndDestinationContainingIgnoreCaseAndDepartureDate(
        String origin, String destination, LocalDate departureDate);
    
    /**
     * Find routes by bus ID
     */
    List<Route> findByBusId(Long busId);
    
    /**
     * Find routes by origin and destination (exact match, case insensitive)
     */
    List<Route> findByOriginIgnoreCaseAndDestinationIgnoreCase(String origin, String destination);
    
    /**
     * Find routes by origin, destination and departure date (exact match, case insensitive)
     */
    List<Route> findByOriginIgnoreCaseAndDestinationIgnoreCaseAndDepartureDate(
        String origin, String destination, LocalDate departureDate);
    
    /**
     * Find routes by departure date only
     */
    List<Route> findByDepartureDate(LocalDate departureDate);
}