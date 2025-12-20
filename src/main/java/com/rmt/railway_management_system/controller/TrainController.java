package com.rmt.railway_management_system.controller;

import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rmt.railway_management_system.entity.Train;
import com.rmt.railway_management_system.service.TrainService;

@RestController
@RequestMapping("/api/trains")
@CrossOrigin(origins = "*")
public class TrainController {

    @Autowired
    private TrainService trainService;

    @GetMapping
    public ResponseEntity<List<Train>> getAllTrains() {
        List<Train> trains = trainService.getAllTrains();
        return ResponseEntity.ok(trains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Train> getTrainById(@PathVariable String id) {
        Train train = trainService.getTrainById(id);
        return ResponseEntity.ok(train);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> searchTrains(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam String date) {
        List<Map<String, Object>> trains = trainService.searchTrains(from, to, date);
        return ResponseEntity.ok(trains);
    }

    @GetMapping("/{trainId}/schedule")
    public ResponseEntity<List<Map<String, Object>>> getTrainSchedule(@PathVariable String trainId) {
        List<Map<String, Object>> schedule = trainService.getTrainSchedule(trainId);
        return ResponseEntity.ok(schedule);
    }

    @GetMapping("/{trainId}/route")
    public ResponseEntity<List<Map<String, Object>>> getTrainRoute(@PathVariable String trainId) {
        List<Map<String, Object>> route = trainService.getTrainRoute(trainId);
        return ResponseEntity.ok(route);
    }

    @PostMapping
    public ResponseEntity<Train> createTrain(@RequestBody Train train) {
        Train createdTrain = trainService.createTrain(train);
        return ResponseEntity.ok(createdTrain);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Train> updateTrain(@PathVariable String id, @RequestBody Train train) {
        train.setTrainId(id);
        Train updatedTrain = trainService.updateTrain(train);
        return ResponseEntity.ok(updatedTrain);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTrain(@PathVariable String id) {
        trainService.deleteTrain(id);
        return ResponseEntity.ok(Map.of("message", "Train deleted successfully"));
    }
}