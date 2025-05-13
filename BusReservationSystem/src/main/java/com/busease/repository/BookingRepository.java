package com.busease.repository;

import com.busease.model.Booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    /**
     * Find bookings by user ID
     */
    List<Booking> findByUserId(Long userId);
    
    /**
     * Find bookings by route ID
     */
    List<Booking> findByRouteId(Long routeId);
    
    /**
     * Find bookings by status
     */
    List<Booking> findByStatus(String status);
    
    /**
     * Find bookings by booking number
     */
    Booking findByBookingNumber(String bookingNumber);
    
    /**
     * Find bookings between dates
     */
    List<Booking> findByBookingDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Count bookings between dates
     */
    long countByBookingDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find bookings by route ID and status
     */
    List<Booking> findByRouteIdAndStatus(Long routeId, String status);
}