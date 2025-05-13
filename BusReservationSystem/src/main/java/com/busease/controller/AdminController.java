package com.busease.controller;

import com.busease.model.Bus;
import com.busease.model.Route;
import com.busease.service.BookingService;
import com.busease.service.BusService;
import com.busease.service.RouteService;
import com.busease.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BusService busService;
    
    @Autowired
    private RouteService routeService;
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private UserService userService;
    
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    
    /**
     * Admin dashboard page - root admin path
     */
    @GetMapping
    public String dashboard(Model model) {
        // Get counts for dashboard stats
        long busCount = busService.countAllBuses();
        long routeCount = routeService.countAllRoutes();
        long todayBookings = bookingService.countTodayBookings();
        long userCount = userService.countAllUsers();
        
        // Add recent activities (dummy data for now)
        List<Map<String, String>> recentActivities = generateDummyActivities();
        
        // Add all data to model
        model.addAttribute("busCount", busCount);
        model.addAttribute("routeCount", routeCount);
        model.addAttribute("todayBookings", todayBookings);
        model.addAttribute("userCount", userCount);
        model.addAttribute("recentActivities", recentActivities);
        
        return "admin/dashboard";
    }
    
    /**
     * Admin dashboard - explicit mapping
     */
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        // Reuse the same logic as the root mapping
        return dashboard(model);
    }
    
    /**
     * Bus management page
     */
    @GetMapping("/buses")
    public String manageBuses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String query,
            Model model) {
        
        try {
            System.out.println("DEBUG: Starting manageBuses method...");
            
            // Define page size
            int pageSize = 10;
            Pageable pageable = PageRequest.of(page, pageSize);
            
            // Get buses with pagination
            Page<Bus> busPage;
            if (query != null && !query.trim().isEmpty()) {
                System.out.println("DEBUG: Searching for buses with query: " + query);
                busPage = busService.searchBuses(query, pageable);
            } else {
                System.out.println("DEBUG: Getting all buses without query");
                busPage = busService.getAllBuses(pageable);
            }
            
            System.out.println("DEBUG: Got " + (busPage != null ? busPage.getTotalElements() : "null") + " buses");
            
            // Add data to model
            model.addAttribute("buses", busPage.getContent());
            model.addAttribute("currentPage", busPage.getNumber() + 1);
            model.addAttribute("totalPages", busPage.getTotalPages());
            
            System.out.println("DEBUG: Returning admin/buses view");
            return "admin/buses";
        } catch (Exception e) {
            System.err.println("ERROR in manageBuses: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Add new bus
     */
    @PostMapping("/buses/add")
    public String addBus(
            @RequestParam String name,
            @RequestParam String registrationNumber,
            @RequestParam String type,
            @RequestParam int capacity,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Create and save the bus
            Bus bus = new Bus();
            bus.setName(name);
            bus.setRegistrationNumber(registrationNumber);
            bus.setType(type);
            bus.setCapacity(capacity);
            bus.setStatus(status);
            
            busService.saveBus(bus);
            
            // Send success message
            redirectAttributes.addFlashAttribute("successMessage", "Bus added successfully!");
        } catch (Exception e) {
            // Send error message
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding bus: " + e.getMessage());
        }
        
        return "redirect:/admin/buses";
    }
    
    /**
     * Edit bus
     */
    @PostMapping("/buses/edit")
    public String editBus(
            @RequestParam Long id,
            @RequestParam String name,
            @RequestParam String registrationNumber,
            @RequestParam String type,
            @RequestParam int capacity,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Get the bus to edit
            Bus bus = busService.getBusById(id);
            if (bus == null) {
                throw new Exception("Bus not found");
            }
            
            // Update bus properties
            bus.setName(name);
            bus.setRegistrationNumber(registrationNumber);
            bus.setType(type);
            bus.setCapacity(capacity);
            bus.setStatus(status);
            
            busService.saveBus(bus);
            
            // Send success message
            redirectAttributes.addFlashAttribute("successMessage", "Bus updated successfully!");
        } catch (Exception e) {
            // Send error message
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating bus: " + e.getMessage());
        }
        
        return "redirect:/admin/buses";
    }
    
    /**
     * Delete bus
     */
    @PostMapping("/buses/delete/{id}")
    public String deleteBus(
            @PathVariable Long id, 
            RedirectAttributes redirectAttributes) {
        
        try {
            busService.deleteBus(id);
            redirectAttributes.addFlashAttribute("successMessage", "Bus deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting bus: " + e.getMessage());
        }
        
        return "redirect:/admin/buses";
    }
    
    /**
     * Route management page
     */
    @GetMapping("/routes")
    public String manageRoutes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String query,
            Model model) {
        
        try {
            System.out.println("DEBUG: Starting manageRoutes method...");
            
            // Define page size
            int pageSize = 10;
            Pageable pageable = PageRequest.of(page, pageSize);
            
            // Get routes with pagination
            Page<Route> routePage;
            if (query != null && !query.trim().isEmpty()) {
                System.out.println("DEBUG: Searching for routes with query: " + query);
                routePage = routeService.searchRoutes(query, pageable);
            } else {
                System.out.println("DEBUG: Getting all routes without query");
                routePage = routeService.getAllRoutes(pageable);
            }
            
            System.out.println("DEBUG: Got " + (routePage != null ? routePage.getTotalElements() : "null") + " routes");
            
            // Get all buses for the dropdown
            List<Bus> buses = busService.getAllBuses();
            System.out.println("DEBUG: Got " + (buses != null ? buses.size() : "null") + " buses");
            
            // Add data to model
            model.addAttribute("routes", routePage.getContent());
            model.addAttribute("buses", buses);
            model.addAttribute("currentPage", routePage.getNumber() + 1);
            model.addAttribute("totalPages", routePage.getTotalPages());
            
            System.out.println("DEBUG: Returning admin/routes view");
            return "admin/routes";
        } catch (Exception e) {
            System.err.println("ERROR in manageRoutes: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Add new route
     */
    @PostMapping("/routes/add")
    public String addRoute(
            @RequestParam String routeCode,
            @RequestParam Long busId,
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam String departureDate,
            @RequestParam String departureTime,
            @RequestParam String arrivalDate,
            @RequestParam String arrivalTime,
            @RequestParam double fare,
            @RequestParam int availableSeats,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Parse dates and times
            LocalDate depDate = LocalDate.parse(departureDate, dateFormatter);
            LocalTime depTime = LocalTime.parse(departureTime, timeFormatter);
            LocalDate arrDate = LocalDate.parse(arrivalDate, dateFormatter);
            LocalTime arrTime = LocalTime.parse(arrivalTime, timeFormatter);
            
            // Get the bus
            Bus bus = busService.getBusById(busId);
            if (bus == null) {
                throw new Exception("Bus not found");
            }
            
            // Create and save the route
            Route route = new Route();
            route.setRouteCode(routeCode);
            route.setBus(bus);
            route.setOrigin(origin);
            route.setDestination(destination);
            route.setDepartureDate(depDate);
            route.setDepartureTime(depTime);
            route.setArrivalDate(arrDate);
            route.setArrivalTime(arrTime);
            route.setFare(fare);
            route.setAvailableSeats(availableSeats);
            
            routeService.saveRoute(route);
            
            // Send success message
            redirectAttributes.addFlashAttribute("successMessage", "Route added successfully!");
        } catch (java.time.format.DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid date or time format");
        } catch (Exception e) {
            // Send error message
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding route: " + e.getMessage());
        }
        
        return "redirect:/admin/routes";
    }
    
    /**
     * Edit route
     */
    @PostMapping("/routes/edit")
    public String editRoute(
            @RequestParam Long id,
            @RequestParam String routeCode,
            @RequestParam Long busId,
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam String departureDate,
            @RequestParam String departureTime,
            @RequestParam String arrivalDate,
            @RequestParam String arrivalTime,
            @RequestParam double fare,
            @RequestParam int availableSeats,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Get the route to edit
            Route route = routeService.getRouteById(id);
            if (route == null) {
                throw new Exception("Route not found");
            }
            
            // Parse dates and times
            LocalDate depDate = LocalDate.parse(departureDate, dateFormatter);
            LocalTime depTime = LocalTime.parse(departureTime, timeFormatter);
            LocalDate arrDate = LocalDate.parse(arrivalDate, dateFormatter);
            LocalTime arrTime = LocalTime.parse(arrivalTime, timeFormatter);
            
            // Get the bus
            Bus bus = busService.getBusById(busId);
            if (bus == null) {
                throw new Exception("Bus not found");
            }
            
            // Update route properties
            route.setRouteCode(routeCode);
            route.setBus(bus);
            route.setOrigin(origin);
            route.setDestination(destination);
            route.setDepartureDate(depDate);
            route.setDepartureTime(depTime);
            route.setArrivalDate(arrDate);
            route.setArrivalTime(arrTime);
            route.setFare(fare);
            route.setAvailableSeats(availableSeats);
            
            routeService.saveRoute(route);
            
            // Send success message
            redirectAttributes.addFlashAttribute("successMessage", "Route updated successfully!");
        } catch (java.time.format.DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid date or time format");
        } catch (Exception e) {
            // Send error message
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating route: " + e.getMessage());
        }
        
        return "redirect:/admin/routes";
    }
    
    /**
     * Delete route
     */
    @PostMapping("/routes/delete/{id}")
    public String deleteRoute(
            @PathVariable Long id, 
            RedirectAttributes redirectAttributes) {
        
        try {
            routeService.deleteRoute(id);
            redirectAttributes.addFlashAttribute("successMessage", "Route deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting route: " + e.getMessage());
        }
        
        return "redirect:/admin/routes";
    }
    
    /**
     * View bookings page
     */
    @GetMapping("/bookings")
    public String manageBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String query,
            Model model) {
        
        try {
            System.out.println("DEBUG: Starting manageBookings method...");
            
            // Define page size
            int pageSize = 10;
            Pageable pageable = PageRequest.of(page, pageSize);
            
            // Get bookings with pagination
            // For now, we'll just get all bookings and add them to the model
            model.addAttribute("bookings", bookingService.getAllBookingsForAdmin(pageable).getContent());
            model.addAttribute("currentPage", page + 1);
            model.addAttribute("totalPages", 1); // For now, just one page
            
            System.out.println("DEBUG: Returning admin/bookings view");
            return "admin/bookings";
        } catch (Exception e) {
            System.err.println("ERROR in manageBookings: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Users management page
     */
    @GetMapping("/users")
    public String manageUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String query,
            Model model) {
        
        try {
            System.out.println("DEBUG: Starting manageUsers method...");
            
            // Define page size
            int pageSize = 10;
            Pageable pageable = PageRequest.of(page, pageSize);
            
            // Get users with pagination
            // For now, we'll just get all users and add them to the model
            model.addAttribute("users", userService.getAllUsers(pageable).getContent());
            model.addAttribute("currentPage", page + 1);
            model.addAttribute("totalPages", 1); // For now, just one page
            
            System.out.println("DEBUG: Returning admin/users view");
            return "admin/users";
        } catch (Exception e) {
            System.err.println("ERROR in manageUsers: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Reports page
     */
    @GetMapping("/reports")
    public String viewReports(Model model) {
        
        try {
            System.out.println("DEBUG: Starting viewReports method...");
            
            // Add dummy report data for now
            model.addAttribute("totalBookings", bookingService.countAllBookings());
            model.addAttribute("totalRevenue", 15000.0); // Dummy value
            model.addAttribute("popularRoutes", routeService.getAllRoutes());
            
            System.out.println("DEBUG: Returning admin/reports view");
            return "admin/reports";
        } catch (Exception e) {
            System.err.println("ERROR in viewReports: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Helper method to generate dummy activities for dashboard
     * In a real app, this would come from a database
     */
    private List<Map<String, String>> generateDummyActivities() {
        List<Map<String, String>> activities = new java.util.ArrayList<>();
        
        Map<String, String> activity1 = new HashMap<>();
        activity1.put("timestamp", "Today, 10:30 AM");
        activity1.put("user", "Admin");
        activity1.put("action", "added a new bus route from Mumbai to Pune");
        activities.add(activity1);
        
        Map<String, String> activity2 = new HashMap<>();
        activity2.put("timestamp", "Today, 09:15 AM");
        activity2.put("user", "Admin");
        activity2.put("action", "updated bus 'Express Deluxe' status to 'Active'");
        activities.add(activity2);
        
        Map<String, String> activity3 = new HashMap<>();
        activity3.put("timestamp", "Yesterday, 3:45 PM");
        activity3.put("user", "Admin");
        activity3.put("action", "added a new bus 'Night Rider'");
        activities.add(activity3);
        
        return activities;
    }
}