package com.busease.service;

import com.busease.model.Booking;
import com.busease.repository.BookingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    
    /**
     * Save a booking
     */
    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }
    
    /**
     * Get a booking by ID
     */
    public Booking getBookingById(Long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        return booking.orElse(null);
    }
    
    /**
     * Delete a booking by ID
     */
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
    
    /**
     * Get all bookings
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    /**
     * Get all bookings with pagination
     */
    public Page<Booking> getAllBookings(Pageable pageable) {
        return bookingRepository.findAll(pageable);
    }
    
    /**
     * Get bookings by user ID
     */
    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }
    
    /**
     * Get today's bookings
     */
    public List<Booking> getTodayBookings() {
        // Get today's date with time set to 00:00:00
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        
        // Get today's date with time set to 23:59:59
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        
        return bookingRepository.findByBookingDateBetween(startOfDay, endOfDay);
    }
    
    /**
     * Count today's bookings
     */
    public long countTodayBookings() {
        // Get today's date with time set to 00:00:00
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        
        // Get today's date with time set to 23:59:59
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        
        return bookingRepository.countByBookingDateBetween(startOfDay, endOfDay);
    }
    
    /**
     * Cancel a booking
     */
    public void cancelBooking(Long id) {
        Booking booking = getBookingById(id);
        if (booking != null) {
            booking.setStatus("Cancelled");
            bookingRepository.save(booking);
        }
    }
    
    /**
     * Get booked seats for a route
     */
    public List<String> getBookedSeatsForRoute(Long routeId) {
        List<String> bookedSeats = new ArrayList<>();
        
        // Get all confirmed bookings for this route
        List<Booking> bookings = bookingRepository.findByRouteIdAndStatus(routeId, "Confirmed");
        
        // Collect all booked seats
        for (Booking booking : bookings) {
            if (booking.getSelectedSeats() != null && !booking.getSelectedSeats().isEmpty()) {
                String[] seats = booking.getSelectedSeats().split(",");
                for (String seat : seats) {
                    bookedSeats.add(seat.trim());
                }
            }
        }
        
        return bookedSeats;
    }
    
    /**
     * Generate a unique booking reference
     */
    public String generateBookingReference() {
        // Generate a random alphanumeric string
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        
        // Current datetime as prefix (yyMMdd)
        LocalDateTime now = LocalDateTime.now();
        String prefix = String.format("%02d%02d%02d", now.getYear() % 100, now.getMonthValue(), now.getDayOfMonth());
        
        // Add prefix
        sb.append(prefix);
        sb.append("-");
        
        // Add 6 random characters
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return sb.toString();
    }
    
    /**
     * Get all bookings for admin view with pagination
     */
    public Page<Booking> getAllBookingsForAdmin(Pageable pageable) {
        return bookingRepository.findAll(pageable);
    }
    
    /**
     * Count all bookings
     */
    public long countAllBookings() {
        return bookingRepository.count();
    }
}