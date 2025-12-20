package com.rmt.railway_management_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rmt.railway_management_system.entity.SeatType;
import com.rmt.railway_management_system.repository.SeatTypeRepository;

@RestController
@RequestMapping("/api/seat-types")
@CrossOrigin(origins = "*")
public class SeatTypeController {

    @Autowired
    private SeatTypeRepository seatTypeRepository;

    @GetMapping
    public ResponseEntity<List<SeatType>> getAllSeatTypes() {
        return ResponseEntity.ok(seatTypeRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatType> getSeatTypeById(@PathVariable Integer id) {
        return seatTypeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SeatType> createSeatType(@RequestBody SeatType seatType) {
        SeatType savedSeatType = seatTypeRepository.save(seatType);
        return ResponseEntity.ok(savedSeatType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeatType> updateSeatType(@PathVariable Integer id, @RequestBody SeatType seatType) {
        if (!seatTypeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        seatType.setTypeId(id);
        SeatType updatedSeatType = seatTypeRepository.save(seatType);
        return ResponseEntity.ok(updatedSeatType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeatType(@PathVariable Integer id) {
        if (seatTypeRepository.existsById(id)) {
            seatTypeRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}