package com.busease.controller;

import com.busease.model.Booking;
import com.busease.model.Passenger;
import com.busease.model.Route;
import com.busease.service.BookingService;
import com.busease.service.RouteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simplified BookingController for student projects.
 * Handles the entire booking process from seat selection to confirmation.
 */
@Controller
@RequestMapping("/booking")
public class BookingController {
    
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private BookingService bookingService;

    @Autowired
    private RouteService routeService;

    /**
     * Redirect old URL pattern to the correct one
     */
    @GetMapping("/seat-selection")
    public String redirectSeatSelection(@RequestParam Long routeId) {
        logger.info("Redirecting from /seat-selection to /booking/select-seats/{}", routeId);
        return "redirect:/booking/select-seats/" + routeId;
    }
    
    /**
     * Redirect from root seat-selection URL to the correct pattern
     */
    @GetMapping("/select-seats")
    public String redirectSeatSelectionFromRoot(@RequestParam Long routeId) {
        logger.info("Redirecting from /booking/select-seats to /booking/select-seats/{}", routeId);
        return "redirect:/booking/select-seats/" + routeId;
    }
    
    /**
     * Show seat selection page
     */
    @GetMapping("/select-seats/{routeId}")
    public String selectSeats(@PathVariable Long routeId, Model model, HttpSession session) {
        try {
            // Check if guest info exists, if not redirect to guest form
            if (session.getAttribute("guestName") == null) {
                return "redirect:/guest-booking";
            }
            
            logger.info("Accessing seat selection page for route ID: {}", routeId);
            Route route = routeService.getRouteById(routeId);
            
            if (route == null) {
                logger.error("Route not found with ID: {}", routeId);
                return "error";
            }
            
            logger.info("Found route: {}", route.toString());
            logger.info("Associated bus: {}", route.getBus() != null ? route.getBus().toString() : "null");
            
            model.addAttribute("route", route);
            List<String> bookedSeats = bookingService.getBookedSeatsForRoute(routeId);
            logger.info("Booked seats: {}", bookedSeats);
            model.addAttribute("bookedSeats", bookedSeats);
            
            return "seat-selection";
        } catch (Exception e) {
            logger.error("Error in select seats method", e);
            return "error";
        }
    }

    /**
     * Simplified passenger details form that uses session data
     */
    @GetMapping("/passenger-details")
    public String showBookingForm(
            @RequestParam Long routeId, 
            @RequestParam String selectedSeats, 
            Model model, 
            HttpSession session) {
        
        // Check if guest info exists, if not redirect to guest form
        if (session.getAttribute("guestName") == null) {
            return "redirect:/guest-booking";
        }
        
        Route route = routeService.getRouteById(routeId);
        
        // Parse selected seats
        String[] seatNumbers = selectedSeats.split(",");
        
        // Create booking with guest info from session
        Booking booking = new Booking();
        booking.setRoute(route);
        booking.setSelectedSeats(selectedSeats);
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus("Pending");
        booking.setSeatCount(seatNumbers.length);
        booking.setTotalFare(route.getFare() * seatNumbers.length);
        
        // Create passenger objects based on number of seats and prefill with guest info
        List<Passenger> passengers = new ArrayList<>();
        for (int i = 0; i < seatNumbers.length; i++) {
            Passenger passenger = new Passenger();
            
            // Only prefill the first passenger with guest info
            if (i == 0) {
                passenger.setName((String) session.getAttribute("guestName"));
                passenger.setEmail((String) session.getAttribute("guestEmail"));
                passenger.setPhone((String) session.getAttribute("guestPhone"));
            }
            
            passengers.add(passenger);
        }
        booking.setPassengers(passengers);
        
        // Add necessary attributes
        model.addAttribute("booking", booking);
        model.addAttribute("route", route);
        model.addAttribute("seatCount", seatNumbers.length);
        model.addAttribute("totalFare", booking.getTotalFare());
        
        return "booking-form";
    }

    /**
     * Simplified booking confirmation that stores passenger and booking info
     */
    @PostMapping("/confirm")
    public String confirmBooking(
            @RequestParam Long routeId,
            @RequestParam String selectedSeats,
            @RequestParam String passengerNames,
            @RequestParam(required = false) String passengerEmail,
            @RequestParam(required = false) String passengerPhone,
            @RequestParam(required = false) String passengerGender,
            @RequestParam(required = false) String passengerAge,
            @RequestParam(required = false, name = "passengerName2") String passengerName2,
            @RequestParam(required = false, name = "passengerEmail2") String passengerEmail2,
            @RequestParam(required = false, name = "passengerPhone2") String passengerPhone2,
            @RequestParam(required = false, name = "passengerGender2") String passengerGender2,
            @RequestParam(required = false, name = "passengerAge2") String passengerAge2,
            @RequestParam(required = false, name = "passengerName3") String passengerName3,
            @RequestParam(required = false, name = "passengerEmail3") String passengerEmail3,
            @RequestParam(required = false, name = "passengerPhone3") String passengerPhone3,
            @RequestParam(required = false, name = "passengerGender3") String passengerGender3,
            @RequestParam(required = false, name = "passengerAge3") String passengerAge3,
            @RequestParam(required = false, name = "passengerName4") String passengerName4,
            @RequestParam(required = false, name = "passengerEmail4") String passengerEmail4,
            @RequestParam(required = false, name = "passengerPhone4") String passengerPhone4,
            @RequestParam(required = false, name = "passengerGender4") String passengerGender4,
            @RequestParam(required = false, name = "passengerAge4") String passengerAge4,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Check if guest info exists
            if (session.getAttribute("guestName") == null) {
                return "redirect:/guest-booking";
            }
            
            logger.info("Starting booking confirmation for route ID: {}, Selected seats: {}", routeId, selectedSeats);
            
            Route route = routeService.getRouteById(routeId);
            String[] seatNumbers = selectedSeats.split(",");
            
            // Generate booking reference first
            String bookingReference = bookingService.generateBookingReference();
            
            // Create booking
            Booking booking = new Booking();
            booking.setRoute(route);
            booking.setSelectedSeats(selectedSeats);
            booking.setBookingDate(LocalDateTime.now());
            booking.setStatus("Confirmed");
            booking.setSeatCount(seatNumbers.length);
            booking.setTotalFare(route.getFare() * seatNumbers.length);
            booking.setBookingNumber(bookingReference);
            
            // Create and add passengers
            List<Passenger> passengers = new ArrayList<>();
            
            // First passenger (required)
            Passenger passenger1 = new Passenger();
            passenger1.setName(passengerNames.split(",")[0].trim());
            passenger1.setSeatNumber(seatNumbers[0].trim());
            passenger1.setEmail(passengerEmail);
            passenger1.setPhone(passengerPhone);
            passenger1.setGender(passengerGender);
            if (passengerAge != null && !passengerAge.isEmpty()) {
                try {
                    passenger1.setAge(Integer.parseInt(passengerAge));
                } catch (NumberFormatException e) {
                    logger.warn("Invalid age format for first passenger");
                }
            }
            passenger1.setBooking(booking);
            passengers.add(passenger1);
            logger.info("Added primary passenger: {}", passenger1);
            
            // Add additional passengers if present
            if (passengerName2 != null && !passengerName2.isEmpty() && seatNumbers.length > 1) {
                Passenger passenger2 = new Passenger();
                passenger2.setName(passengerName2.trim());
                passenger2.setSeatNumber(seatNumbers[1].trim());
                passenger2.setEmail(passengerEmail2);
                passenger2.setPhone(passengerPhone2);
                passenger2.setGender(passengerGender2);
                if (passengerAge2 != null && !passengerAge2.isEmpty()) {
                    try {
                        passenger2.setAge(Integer.parseInt(passengerAge2));
                    } catch (NumberFormatException e) {
                        logger.warn("Invalid age format for second passenger");
                    }
                }
                passenger2.setBooking(booking);
                passengers.add(passenger2);
                logger.info("Added passenger 2: {}", passenger2);
            }
            
            if (passengerName3 != null && !passengerName3.isEmpty() && seatNumbers.length > 2) {
                Passenger passenger3 = new Passenger();
                passenger3.setName(passengerName3.trim());
                passenger3.setSeatNumber(seatNumbers[2].trim());
                passenger3.setEmail(passengerEmail3);
                passenger3.setPhone(passengerPhone3);
                passenger3.setGender(passengerGender3);
                if (passengerAge3 != null && !passengerAge3.isEmpty()) {
                    try {
                        passenger3.setAge(Integer.parseInt(passengerAge3));
                    } catch (NumberFormatException e) {
                        logger.warn("Invalid age format for third passenger");
                    }
                }
                passenger3.setBooking(booking);
                passengers.add(passenger3);
                logger.info("Added passenger 3: {}", passenger3);
            }
            
            if (passengerName4 != null && !passengerName4.isEmpty() && seatNumbers.length > 3) {
                Passenger passenger4 = new Passenger();
                passenger4.setName(passengerName4.trim());
                passenger4.setSeatNumber(seatNumbers[3].trim());
                passenger4.setEmail(passengerEmail4);
                passenger4.setPhone(passengerPhone4);
                passenger4.setGender(passengerGender4);
                if (passengerAge4 != null && !passengerAge4.isEmpty()) {
                    try {
                        passenger4.setAge(Integer.parseInt(passengerAge4));
                    } catch (NumberFormatException e) {
                        logger.warn("Invalid age format for fourth passenger");
                    }
                }
                passenger4.setBooking(booking);
                passengers.add(passenger4);
                logger.info("Added passenger 4: {}", passenger4);
            }
            
            // Add passengers to booking
            booking.setPassengers(passengers);
            
            // Save booking with cascaded passengers
            logger.info("Saving booking with {} passengers", passengers.size());
            Booking savedBooking = bookingService.saveBooking(booking);
            logger.info("Booking saved: {}", savedBooking);
            
            // Store confirmation info in session
            session.setAttribute("lastBookingId", savedBooking.getId());
            session.setAttribute("lastBookingReference", bookingReference);
            
            // Update available seats on the route
            route.setAvailableSeats(route.getAvailableSeats() - seatNumbers.length);
            routeService.saveRoute(route);
            
            // Add info to redirect
            redirectAttributes.addFlashAttribute("bookingReference", bookingReference);
            redirectAttributes.addFlashAttribute("booking", savedBooking);
            redirectAttributes.addFlashAttribute("route", route);
            redirectAttributes.addFlashAttribute("seatCount", seatNumbers.length);
            redirectAttributes.addFlashAttribute("totalFare", savedBooking.getTotalFare());
            redirectAttributes.addFlashAttribute("passengerNames", passengerNames);
            
            return "redirect:/booking/confirmation";
        } catch (Exception e) {
            logger.error("Error confirming booking", e);
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "There was a problem processing your booking: " + e.getMessage());
            return "redirect:/error";
        }
    }
    
    /**
     * Display booking confirmation page
     */
    @GetMapping("/confirmation")
    public String showConfirmation(Model model) {
        if (!model.containsAttribute("bookingReference")) {
            return "redirect:/";
        }
        return "booking-confirmation";
    }
    
    /**
     * Cancel a booking (simplified)
     */
    @GetMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookingService.cancelBooking(id);
            redirectAttributes.addFlashAttribute("message", "Booking cancelled successfully!");
        } catch (Exception e) {
            logger.error("Error cancelling booking", e);
            redirectAttributes.addFlashAttribute("error", "Could not cancel booking.");
        }
        return "redirect:/";
    }
}
