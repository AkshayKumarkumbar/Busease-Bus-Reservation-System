package com.busease.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for handling basic navigation pages.
 */
@Controller
public class HomeController {
    
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    /**
     * Displays the home page.
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "Welcome to Busease");
        return "index";
    }
    
    /**
     * Displays the error page.
     */
    @GetMapping("/error")
    public String error(Model model) {
        model.addAttribute("pageTitle", "Error - Busease");
        return "error";
    }
    
    /**
     * Simple health check endpoint that returns plain text instead of HTML.
     * This is useful for checking if the application is running correctly.
     */
    @GetMapping("/health")
    @ResponseBody
    public String healthCheck() {
        return "Busease application is running!";
    }
    
    /**
     * Redirect from root seat-selection URL to the correct pattern
     * This handles the legacy URL pattern that may be used in links
     */
    @GetMapping("/seat-selection")
    public String redirectSeatSelection(@RequestParam Long routeId) {
        logger.info("Redirecting from root /seat-selection to /booking/select-seats/{}", routeId);
        return "redirect:/booking/select-seats/" + routeId;
    }
}
