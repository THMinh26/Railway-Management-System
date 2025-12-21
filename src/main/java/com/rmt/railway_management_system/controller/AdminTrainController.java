package com.rmt.railway_management_system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.rmt.railway_management_system.dto.ErrorResponseDTO;
import com.rmt.railway_management_system.dto.TrainRequestDTO;
import com.rmt.railway_management_system.entity.Train;
import com.rmt.railway_management_system.repository.TrainRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/trains")
@Validated
@CrossOrigin(origins = "*")
public class AdminTrainController {

    @Autowired
    private TrainRepository trainRepository;

    /**
     * UC13: View All Trains
     * GET /api/admin/trains
     */
    @GetMapping
    public ResponseEntity<?> getAllTrains() {
        try {
            List<Train> trains = trainRepository.findAll();
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Trains retrieved successfully");
            response.put("count", trains.size());
            response.put("trains", trains);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            "Failed to retrieve trains",
                            e.getMessage()));
        }
    }

    /**
     * UC14: Add Train
     * POST /api/admin/trains
     */
    @PostMapping
    public ResponseEntity<?> addTrain(@Valid @RequestBody TrainRequestDTO request) {
        try {
            // Check if train ID already exists
            if (trainRepository.existsById(request.getTrainId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorResponseDTO(HttpStatus.CONFLICT.value(),
                                "Conflict",
                                "Train ID already exists",
                                "Train with ID " + request.getTrainId() + " already exists"));
            }

            Train train = new Train(request.getTrainId(), request.getTrainName());
            Train savedTrain = trainRepository.save(train);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Train added successfully");
            response.put("train", savedTrain);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            "Failed to add train",
                            e.getMessage()));
        }
    }

    /**
     * UC15: Delete Train
     * DELETE /api/admin/trains/{trainId}
     */
    @DeleteMapping("/{trainId}")
    public ResponseEntity<?> deleteTrain(@PathVariable String trainId) {
        try {
            if (!trainRepository.existsById(trainId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(),
                                "Not Found",
                                "Train not found",
                                "Train with ID " + trainId + " not found"));
            }

            // TODO: Add validation to check if train has active bookings
            // TODO: Delete associated schedules and coaches
            
            trainRepository.deleteById(trainId);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Train deleted successfully");
            response.put("trainId", trainId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            "Failed to delete train",
                            e.getMessage()));
        }
    }

    /**
     * Get single train by ID
     * GET /api/admin/trains/{trainId}
     */
    @GetMapping("/{trainId}")
    public ResponseEntity<?> getTrainById(@PathVariable String trainId) {
        try {
            Train train = trainRepository.findById(trainId)
                    .orElseThrow(() -> new RuntimeException("Train not found"));

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Train retrieved successfully");
            response.put("train", train);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(),
                            "Not Found",
                            "Train not found",
                            e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            "Failed to retrieve train",
                            e.getMessage()));
        }
    }
}
