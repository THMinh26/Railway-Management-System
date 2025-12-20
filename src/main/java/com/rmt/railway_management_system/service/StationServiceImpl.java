package com.rmt.railway_management_system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rmt.railway_management_system.entity.Station;
import com.rmt.railway_management_system.exception.DuplicateResourceException;
import com.rmt.railway_management_system.exception.ResourceNotFoundException;
import com.rmt.railway_management_system.repository.StationRepository;

@Service
public class StationServiceImpl implements StationService {

    @Autowired
    private StationRepository stationRepository;

    @Override
    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }

    @Override
    public Station getStationById(String stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new ResourceNotFoundException("Station not found with ID: " + stationId));
    }

    @Override
    public List<Station> searchStationsByName(String query) {
        return stationRepository.searchByName(query);
    }

    @Override
    public Station createStation(Station station) {
        if (stationRepository.existsById(station.getStationId())) {
            throw new DuplicateResourceException("Station already exists with ID: " + station.getStationId());
        }
        return stationRepository.save(station);
    }

    @Override
    public Station updateStation(Station station) {
        if (!stationRepository.existsById(station.getStationId())) {
            throw new ResourceNotFoundException("Station not found with ID: " + station.getStationId());
        }
        return stationRepository.save(station);
    }

    @Override
    public void deleteStation(String stationId) {
        if (!stationRepository.existsById(stationId)) {
            throw new ResourceNotFoundException("Station not found with ID: " + stationId);
        }
        stationRepository.deleteById(stationId);
    }
}