package com.busease.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * REST API controller for health checks and application status.
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    /**
     * Simple health check endpoint.
     * @return A status message with timestamp
     */
    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("message", "Busease application is running!");
        status.put("timestamp", LocalDateTime.now().toString());
        return status;
    }
}