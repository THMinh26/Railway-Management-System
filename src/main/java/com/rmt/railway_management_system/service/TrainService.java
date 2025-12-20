package com.rmt.railway_management_system.service;

import java.util.List;
import java.util.Map;

import com.rmt.railway_management_system.entity.Train;

public interface TrainService {
    List<Train> getAllTrains();

    Train getTrainById(String trainId);

    List<Map<String, Object>> searchTrains(String fromStationId, String toStationId, String date);

    List<Map<String, Object>> getTrainSchedule(String trainId);

    List<Map<String, Object>> getTrainRoute(String trainId);

    Train createTrain(Train train);

    Train updateTrain(Train train);

    void deleteTrain(String trainId);
}