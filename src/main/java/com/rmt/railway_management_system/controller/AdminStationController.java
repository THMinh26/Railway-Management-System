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
import com.rmt.railway_management_system.dto.StationRequestDTO;
import com.rmt.railway_management_system.entity.Station;
import com.rmt.railway_management_system.repository.StationRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/stations")
@Validated
@CrossOrigin(origins = "*")
public class AdminStationController {

    @Autowired
    private StationRepository stationRepository;

    /**
     * UC10: View All Stations
     * GET /api/admin/stations
     */
    @GetMapping
    public ResponseEntity<?> getAllStations() {
        try {
            List<Station> stations = stationRepository.findAll();
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Stations retrieved successfully");
            response.put("count", stations.size());
            response.put("stations", stations);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", "Failed to retrieve stations", e.getMessage()));
        }
    }

    /**
     * UC11: Add Station
     * POST /api/admin/stations
     */
    @PostMapping
    public ResponseEntity<?> addStation(@Valid @RequestBody StationRequestDTO request) {
        try {
            // Check if station ID already exists
            if (stationRepository.existsById(request.getStationId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorResponseDTO(HttpStatus.CONFLICT.value(), "Conflict", "Station ID already exists", 
                                "Station with ID " + request.getStationId() + " already exists"));
            }

            // Check if station name already exists
            if (stationRepository.findByName(request.getName()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorResponseDTO(HttpStatus.CONFLICT.value(), "Conflict", "Station name already exists", 
                                "Station with name " + request.getName() + " already exists"));
            }

            Station station = new Station(request.getStationId(), request.getName());
            Station savedStation = stationRepository.save(station);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Station added successfully");
            response.put("station", savedStation);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", "Failed to add station", e.getMessage()));
        }
    }

    /**
     * UC12: Delete Station
     * DELETE /api/admin/stations/{stationId}
     */
    @DeleteMapping("/{stationId}")
    public ResponseEntity<?> deleteStation(@PathVariable String stationId) {
        try {
            if (!stationRepository.existsById(stationId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), "Not Found", "Station not found", 
                                "Station with ID " + stationId + " not found"));
            }

            // TODO: Add validation to check if station is referenced in active schedules
            // This would require querying schedules and checking for references
            
            stationRepository.deleteById(stationId);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Station deleted successfully");
            response.put("stationId", stationId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", "Failed to delete station", e.getMessage()));
        }
    }

    /**
     * Get single station by ID
     * GET /api/admin/stations/{stationId}
     */
    @GetMapping("/{stationId}")
    public ResponseEntity<?> getStationById(@PathVariable String stationId) {
        try {
            Station station = stationRepository.findById(stationId)
                    .orElseThrow(() -> new RuntimeException("Station not found"));

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Station retrieved successfully");
            response.put("station", station);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), "Not Found", "Station not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", "Failed to retrieve station", e.getMessage()));
        }
    }
}
