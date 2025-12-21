package com.rmt.railway_management_system.controller;

import com.rmt.railway_management_system.dto.TrainSearchRequestDTO;
import com.rmt.railway_management_system.dto.TrainSearchResponseDTO;
import com.rmt.railway_management_system.service.TrainService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trains")
@CrossOrigin(origins = "*")
public class TrainController {

    @Autowired
    private TrainService trainService;

    @PostMapping("/search")
    public ResponseEntity<List<TrainSearchResponseDTO>> searchTrains(@RequestBody TrainSearchRequestDTO request) {
        List<TrainSearchResponseDTO> trains = trainService.searchTrains(
                request.getSource(),
                request.getDestination());
        return ResponseEntity.ok(trains);
    }
}