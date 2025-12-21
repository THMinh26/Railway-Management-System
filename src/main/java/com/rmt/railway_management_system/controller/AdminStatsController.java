package com.rmt.railway_management_system.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rmt.railway_management_system.repository.BookingRepository;
import com.rmt.railway_management_system.repository.TrainRepository;
import com.rmt.railway_management_system.repository.UserRepository;

@RestController
@RequestMapping("/api/admin/stats")
@CrossOrigin(origins = "*")
public class AdminStatsController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainRepository trainRepository;

    /**
     * Get admin statistics
     * GET /api/admin/stats
     */
    @GetMapping
    public ResponseEntity<?> getStatistics() {
        try {
            long totalBookings = bookingRepository.count();
            long totalUsers = userRepository.count();
            long totalTrains = trainRepository.count();
            long cancelledBookings = bookingRepository.findAll().stream()
                    .filter(b -> "CANCELLED".equalsIgnoreCase(b.getStatus()))
                    .count();

            Map<String, Object> stats = new HashMap<>();
            stats.put("totalBookings", totalBookings);
            stats.put("totalUsers", totalUsers);
            stats.put("totalTrains", totalTrains);
            stats.put("cancelledBookings", cancelledBookings);

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to retrieve statistics");
            error.put("message", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
