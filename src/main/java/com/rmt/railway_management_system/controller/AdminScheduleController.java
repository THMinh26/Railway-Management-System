package com.rmt.railway_management_system.controller;

import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.rmt.railway_management_system.dto.ErrorResponseDTO;
import com.rmt.railway_management_system.dto.ScheduleRequestDTO;
import com.rmt.railway_management_system.dto.ScheduleResponseDTO;
import com.rmt.railway_management_system.entity.Schedule;
import com.rmt.railway_management_system.entity.Station;
import com.rmt.railway_management_system.entity.Train;
import com.rmt.railway_management_system.repository.ScheduleRepository;
import com.rmt.railway_management_system.repository.StationRepository;
import com.rmt.railway_management_system.repository.TrainRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/schedules")
@Validated
@CrossOrigin(origins = "*")
public class AdminScheduleController {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private StationRepository stationRepository;

    /**
     * View All Schedules
     * GET /api/admin/schedules
     */
    @GetMapping
    public ResponseEntity<?> getAllSchedules() {
        try {
            List<Schedule> schedules = scheduleRepository.findAll();
            
            List<ScheduleResponseDTO> scheduleResponses = schedules.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Schedules retrieved successfully");
            response.put("count", scheduleResponses.size());
            response.put("schedules", scheduleResponses);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO("Failed to retrieve schedules", e.getMessage()));
        }
    }

    /**
     * UC16: Create Schedule
     * POST /api/admin/schedules
     */
    @PostMapping
    public ResponseEntity<?> createSchedule(@Valid @RequestBody ScheduleRequestDTO request) {
        try {
            // Verify train exists
            Train train = trainRepository.findById(request.getTrainId())
                    .orElseThrow(() -> new RuntimeException("Train not found"));

            // Verify station exists
            Station station = stationRepository.findById(request.getStationId())
                    .orElseThrow(() -> new RuntimeException("Station not found"));

            // Check if schedule already exists
            Schedule.ScheduleId scheduleId = new Schedule.ScheduleId(request.getTrainId(), request.getStationId());
            if (scheduleRepository.existsById(scheduleId)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorResponseDTO("Schedule already exists", 
                                "Schedule for train " + request.getTrainId() + " at station " + request.getStationId() + " already exists"));
            }

            // Parse times
            Time timeIn = Time.valueOf(request.getTimeIn());
            Time timeOut = Time.valueOf(request.getTimeOut());

            // Create schedule
            Schedule schedule = new Schedule();
            schedule.setId(scheduleId);
            schedule.setTrain(train);
            schedule.setStation(station);
            schedule.setSequenceNo(request.getSequenceNo());
            schedule.setTimeIn(timeIn);
            schedule.setTimeOut(timeOut);

            Schedule savedSchedule = scheduleRepository.save(schedule);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Schedule created successfully");
            response.put("schedule", convertToDTO(savedSchedule));
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDTO("Invalid time format", "Time format must be HH:mm:ss"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDTO("Resource not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO("Failed to create schedule", e.getMessage()));
        }
    }

    /**
     * UC17: Update Schedule
     * PUT /api/admin/schedules/{trainId}/{stationId}
     */
    @PutMapping("/{trainId}/{stationId}")
    public ResponseEntity<?> updateSchedule(
            @PathVariable String trainId,
            @PathVariable String stationId,
            @Valid @RequestBody ScheduleRequestDTO request) {
        try {
            Schedule.ScheduleId scheduleId = new Schedule.ScheduleId(trainId, stationId);
            Schedule schedule = scheduleRepository.findById(scheduleId)
                    .orElseThrow(() -> new RuntimeException("Schedule not found"));

            // Parse times
            Time timeIn = Time.valueOf(request.getTimeIn());
            Time timeOut = Time.valueOf(request.getTimeOut());

            // Update schedule
            schedule.setSequenceNo(request.getSequenceNo());
            schedule.setTimeIn(timeIn);
            schedule.setTimeOut(timeOut);

            Schedule updatedSchedule = scheduleRepository.save(schedule);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Schedule updated successfully");
            response.put("schedule", convertToDTO(updatedSchedule));
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDTO("Invalid time format", "Time format must be HH:mm:ss"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDTO("Schedule not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO("Failed to update schedule", e.getMessage()));
        }
    }

    /**
     * UC18: Delete Schedule
     * DELETE /api/admin/schedules/{trainId}/{stationId}
     */
    @DeleteMapping("/{trainId}/{stationId}")
    public ResponseEntity<?> deleteSchedule(
            @PathVariable String trainId,
            @PathVariable String stationId) {
        try {
            Schedule.ScheduleId scheduleId = new Schedule.ScheduleId(trainId, stationId);
            
            if (!scheduleRepository.existsById(scheduleId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponseDTO("Schedule not found", 
                                "Schedule for train " + trainId + " at station " + stationId + " not found"));
            }

            // TODO: Add validation to check if active bookings depend on this schedule
            
            scheduleRepository.deleteById(scheduleId);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Schedule deleted successfully");
            response.put("trainId", trainId);
            response.put("stationId", stationId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO("Failed to delete schedule", e.getMessage()));
        }
    }

    /**
     * Get schedules by train ID
     * GET /api/admin/schedules/train/{trainId}
     */
    @GetMapping("/train/{trainId}")
    public ResponseEntity<?> getSchedulesByTrainId(@PathVariable String trainId) {
        try {
            List<Schedule> schedules = scheduleRepository.findAll().stream()
                    .filter(s -> s.getTrain().getTrainId().equals(trainId))
                    .collect(Collectors.toList());
            
            List<ScheduleResponseDTO> scheduleResponses = schedules.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Schedules retrieved successfully");
            response.put("count", scheduleResponses.size());
            response.put("schedules", scheduleResponses);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO("Failed to retrieve schedules", e.getMessage()));
        }
    }

    private ScheduleResponseDTO convertToDTO(Schedule schedule) {
        return new ScheduleResponseDTO(
                schedule.getTrain().getTrainId(),
                schedule.getTrain().getTrainName(),
                schedule.getStation().getStationId(),
                schedule.getStation().getName(),
                schedule.getSequenceNo(),
                schedule.getTimeIn().toString(),
                schedule.getTimeOut().toString()
        );
    }
}
