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

import com.rmt.railway_management_system.entity.Coach;
import com.rmt.railway_management_system.repository.CoachRepository;

@RestController
@RequestMapping("/api/coaches")
@CrossOrigin(origins = "*")
public class CoachController {

    @Autowired
    private CoachRepository coachRepository;

    @GetMapping
    public ResponseEntity<List<Coach>> getAllCoaches() {
        return ResponseEntity.ok(coachRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coach> getCoachById(@PathVariable String id) {
        return coachRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/train/{trainId}")
    public ResponseEntity<List<Coach>> getCoachesByTrainId(@PathVariable String trainId) {
        return ResponseEntity.ok(coachRepository.findByTrainId(trainId));
    }

    @PostMapping
    public ResponseEntity<Coach> createCoach(@RequestBody Coach coach) {
        Coach savedCoach = coachRepository.save(coach);
        return ResponseEntity.ok(savedCoach);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Coach> updateCoach(@PathVariable String id, @RequestBody Coach coach) {
        if (!coachRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        coach.setCoachId(id);
        Coach updatedCoach = coachRepository.save(coach);
        return ResponseEntity.ok(updatedCoach);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoach(@PathVariable String id) {
        if (coachRepository.existsById(id)) {
            coachRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}