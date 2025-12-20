package com.rmt.railway_management_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rmt.railway_management_system.entity.Seat;
import com.rmt.railway_management_system.repository.SeatRepository;

@RestController
@RequestMapping("/api/seats")
@CrossOrigin(origins = "*")
public class SeatController {

    @Autowired
    private SeatRepository seatRepository;

    @GetMapping
    public ResponseEntity<List<Seat>> getAllSeats() {
        return ResponseEntity.ok(seatRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seat> getSeatById(@PathVariable String id) {
        return seatRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/coach/{coachId}")
    public ResponseEntity<List<Seat>> getSeatsByCoachId(@PathVariable String coachId) {
        return ResponseEntity.ok(seatRepository.findByCoachId(coachId));
    }

    @PostMapping
    public ResponseEntity<Seat> createSeat(@RequestBody Seat seat) {
        Seat savedSeat = seatRepository.save(seat);
        return ResponseEntity.ok(savedSeat);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable String id) {
        if (seatRepository.existsById(id)) {
            seatRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}