package com.rmt.railway_management_system.service;

import java.util.List;

import com.rmt.railway_management_system.entity.Station;

public interface StationService {
    List<Station> getAllStations();

    Station getStationById(String stationId);

    List<Station> searchStationsByName(String query);

    Station createStation(Station station);

    Station updateStation(Station station);

    void deleteStation(String stationId);
}