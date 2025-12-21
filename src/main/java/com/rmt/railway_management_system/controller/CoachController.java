package com.rmt.railway_management_system.controller;

import com.rmt.railway_management_system.dto.CoachDTO;
import com.rmt.railway_management_system.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/coaches")
@CrossOrigin(origins = "*")
public class CoachController {

    @Autowired
    private CoachService coachService;

    @GetMapping("/{trainId}")
    public ResponseEntity<List<CoachDTO>> getCoachesByTrainId(
            @PathVariable String trainId,
            @RequestParam String date) {

        Date travelDate = Date.valueOf(date);
        List<CoachDTO> coaches = coachService.getCoachesByTrainId(trainId, travelDate);
        return ResponseEntity.ok(coaches);
    }
}