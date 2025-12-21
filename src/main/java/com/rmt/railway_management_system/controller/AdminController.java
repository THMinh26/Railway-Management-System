package com.rmt.railway_management_system.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    /**
     * Admin API Index - Lists all available admin endpoints
     * GET /api/admin
     */
    @GetMapping
    public ResponseEntity<?> getAdminInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Railway Management System - Admin API");
        response.put("version", "1.0.0");
        
        Map<String, Object> endpoints = new HashMap<>();
        
        // Stations
        Map<String, String> stations = new HashMap<>();
        stations.put("GET /api/admin/stations", "View all stations");
        stations.put("GET /api/admin/stations/{id}", "Get station by ID");
        stations.put("POST /api/admin/stations", "Add new station");
        stations.put("DELETE /api/admin/stations/{id}", "Delete station");
        endpoints.put("Stations", stations);
        
        // Trains
        Map<String, String> trains = new HashMap<>();
        trains.put("GET /api/admin/trains", "View all trains");
        trains.put("GET /api/admin/trains/{id}", "Get train by ID");
        trains.put("POST /api/admin/trains", "Add new train");
        trains.put("DELETE /api/admin/trains/{id}", "Delete train");
        endpoints.put("Trains", trains);
        
        // Schedules
        Map<String, String> schedules = new HashMap<>();
        schedules.put("GET /api/admin/schedules", "View all schedules");
        schedules.put("GET /api/admin/schedules/train/{trainId}", "Get schedules by train");
        schedules.put("POST /api/admin/schedules", "Create new schedule");
        schedules.put("PUT /api/admin/schedules/{trainId}/{stationId}", "Update schedule");
        schedules.put("DELETE /api/admin/schedules/{trainId}/{stationId}", "Delete schedule");
        endpoints.put("Schedules", schedules);
        
        // Users
        Map<String, String> users = new HashMap<>();
        users.put("GET /api/admin/users", "View all users");
        users.put("GET /api/admin/users/{id}", "Get user by ID");
        endpoints.put("Users", users);
        
        // Bookings
        Map<String, String> bookings = new HashMap<>();
        bookings.put("GET /api/admin/bookings", "View all bookings");
        bookings.put("GET /api/admin/bookings/{id}", "Get booking by ID");
        bookings.put("GET /api/admin/bookings/user/{userId}", "Get bookings by user");
        bookings.put("DELETE /api/admin/bookings/{id}", "Delete booking");
        endpoints.put("Bookings", bookings);
        
        response.put("endpoints", endpoints);
        response.put("documentation", "/ADMIN_API_DOCUMENTATION.md");
        
        return ResponseEntity.ok(response);
    }
}
