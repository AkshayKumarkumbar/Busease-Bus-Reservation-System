package com.busease.controller;

import com.busease.model.Bus;
import com.busease.service.BusService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/buses")
public class BusController {

    @Autowired
    private BusService busService;

    @GetMapping
    public String listAllBuses(Model model) {
        List<Bus> buses = busService.getAllBuses();
        model.addAttribute("buses", buses);
        return "bus-list";
    }

    @GetMapping("/details")
    public String getBusDetails(@RequestParam Long id, Model model) {
        Bus bus = busService.getBusById(id);
        model.addAttribute("bus", bus);
        return "bus-details";
    }
}
