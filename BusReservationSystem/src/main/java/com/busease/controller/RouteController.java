package com.busease.controller;

import com.busease.model.Route;
import com.busease.service.RouteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/api/routes")
public class RouteController {

    @Autowired
    private RouteService routeService;

    /**
     * Simple API endpoint to get all routes as JSON
     */
    @GetMapping(produces = "application/json")
    @ResponseBody
    public List<Route> listRoutesJson() {
        return routeService.getAllRoutes();
    }
}
