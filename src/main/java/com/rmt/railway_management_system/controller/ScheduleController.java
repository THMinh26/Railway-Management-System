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

import com.rmt.railway_management_system.entity.Schedule;
import com.rmt.railway_management_system.repository.ScheduleRepository;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin(origins = "*")
public class ScheduleController {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping
    public ResponseEntity<List<Schedule>> getAllSchedules() {
        return ResponseEntity.ok(scheduleRepository.findAll());
    }

    @GetMapping("/{trainId}/{stationId}")
    public ResponseEntity<Schedule> getScheduleById(
            @PathVariable String trainId,
            @PathVariable String stationId) {
        Schedule.ScheduleId id = new Schedule.ScheduleId(trainId, stationId);
        return scheduleRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/train/{trainId}")
    public ResponseEntity<List<Schedule>> getSchedulesByTrainId(@PathVariable String trainId) {
        return ResponseEntity.ok(scheduleRepository.findByTrainIdOrderBySequenceNo(trainId));
    }

    @PostMapping
    public ResponseEntity<Schedule> createSchedule(@RequestBody Schedule schedule) {
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return ResponseEntity.ok(savedSchedule);
    }

    @DeleteMapping("/{trainId}/{stationId}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable String trainId,
            @PathVariable String stationId) {
        Schedule.ScheduleId id = new Schedule.ScheduleId(trainId, stationId);
        if (scheduleRepository.existsById(id)) {
            scheduleRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}