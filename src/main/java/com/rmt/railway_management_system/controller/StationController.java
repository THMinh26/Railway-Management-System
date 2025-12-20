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

import com.rmt.railway_management_system.entity.Station;
import com.rmt.railway_management_system.service.StationService;

@RestController
@RequestMapping("/api/stations")
@CrossOrigin(origins = "*")
public class StationController {

    @Autowired
    private StationService stationService;

    @GetMapping
    public ResponseEntity<List<Station>> getAllStations() {
        List<Station> stations = stationService.getAllStations();
        return ResponseEntity.ok(stations);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Station>> searchStations(@RequestParam String query) {
        List<Station> stations = stationService.searchStationsByName(query);
        return ResponseEntity.ok(stations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Station> getStationById(@PathVariable String id) {
        Station station = stationService.getStationById(id);
        return ResponseEntity.ok(station);
    }

    @PostMapping
    public ResponseEntity<Station> createStation(@RequestBody Station station) {
        Station createdStation = stationService.createStation(station);
        return ResponseEntity.ok(createdStation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Station> updateStation(@PathVariable String id, @RequestBody Station station) {
        station.setStationId(id);
        Station updatedStation = stationService.updateStation(station);
        return ResponseEntity.ok(updatedStation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteStation(@PathVariable String id) {
        stationService.deleteStation(id);
        return ResponseEntity.ok(Map.of("message", "Station deleted successfully"));
    }
}