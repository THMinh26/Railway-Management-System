package com.rmt.railway_management_system.controller;

import com.rmt.railway_management_system.dto.SeatDTO;
import com.rmt.railway_management_system.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/seats")
@CrossOrigin(origins = "*")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @GetMapping("/{coachId}")
    public ResponseEntity<List<SeatDTO>> getAvailableSeats(
            @PathVariable String coachId,
            @RequestParam String date) {

        Date travelDate = java.sql.Date.valueOf(date);
        List<SeatDTO> seats = seatService.getAvailableSeatsByCoachAndDate(coachId, travelDate);
        return ResponseEntity.ok(seats);
    }
}