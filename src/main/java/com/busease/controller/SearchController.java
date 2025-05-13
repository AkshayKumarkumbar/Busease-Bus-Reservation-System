package com.busease.controller;

import com.busease.model.Route;
import com.busease.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

/**
 * Simplified search controller for student projects.
 * Provides a simple interface to search for bus routes.
 */
@Controller
public class SearchController {

    @Autowired
    private RouteService routeService;

    /**
     * Display the search form
     */
    @GetMapping("/search")
    public String showSearchForm(Model model) {
        model.addAttribute("pageTitle", "Search Buses - Busease");
        return "search";
    }

    /**
     * Process the search and display results
     */
    @GetMapping("/search-results")
    public String searchRoutes(
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate travelDate,
            Model model,
            HttpSession session) {
        
        List<Route> routes;
        
        // If all parameters are provided, perform specific search
        if (origin != null && !origin.isEmpty() && destination != null && !destination.isEmpty() && travelDate != null) {
            routes = routeService.findRoutesByOriginDestinationAndDate(origin, destination, travelDate);
            model.addAttribute("searchCriteria", "From: " + origin + " | To: " + destination + " | Date: " + travelDate);
        } 
        // If only origin and destination are provided
        else if (origin != null && !origin.isEmpty() && destination != null && !destination.isEmpty()) {
            routes = routeService.findRoutesByOriginAndDestination(origin, destination);
            model.addAttribute("searchCriteria", "From: " + origin + " | To: " + destination);
        }
        // If no or incomplete parameters, show all available routes
        else {
            routes = routeService.getAllRoutes();
            model.addAttribute("searchCriteria", "All available routes");
        }
        
        // Check if user has guest session, if not let them know they'll need to provide info
        boolean hasGuestSession = session.getAttribute("guestName") != null;
        
        model.addAttribute("routes", routes);
        model.addAttribute("origin", origin);
        model.addAttribute("destination", destination);
        model.addAttribute("travelDate", travelDate);
        model.addAttribute("pageTitle", "Search Results - Busease");
        model.addAttribute("hasGuestSession", hasGuestSession);
        
        return "search-results";
    }
}