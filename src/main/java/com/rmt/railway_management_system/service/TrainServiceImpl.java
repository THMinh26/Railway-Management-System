package com.rmt.railway_management_system.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rmt.railway_management_system.entity.Schedule;
import com.rmt.railway_management_system.entity.Train;
import com.rmt.railway_management_system.exception.DuplicateResourceException;
import com.rmt.railway_management_system.exception.ResourceNotFoundException;
import com.rmt.railway_management_system.repository.ScheduleRepository;
import com.rmt.railway_management_system.repository.TrainRepository;

@Service
public class TrainServiceImpl implements TrainService {

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }

    @Override
    public Train getTrainById(String trainId) {
        return trainRepository.findById(trainId)
                .orElseThrow(() -> new ResourceNotFoundException("Train not found with ID: " + trainId));
    }

    @Override
    public List<Map<String, Object>> searchTrains(String fromStationId, String toStationId, String date) {
        List<Schedule> allSchedules = scheduleRepository.findAll();
        Map<String, List<Schedule>> schedulesByTrain = allSchedules.stream()
                .collect(Collectors.groupingBy(s -> s.getTrain().getTrainId()));

        List<Map<String, Object>> results = new ArrayList<>();

        for (Map.Entry<String, List<Schedule>> entry : schedulesByTrain.entrySet()) {
            List<Schedule> trainSchedules = entry.getValue();
            trainSchedules.sort(Comparator.comparingInt(Schedule::getSequenceNo));

            Schedule fromSchedule = null;
            Schedule toSchedule = null;

            for (Schedule schedule : trainSchedules) {
                if (schedule.getStation().getStationId().equals(fromStationId)) {
                    fromSchedule = schedule;
                }
                if (schedule.getStation().getStationId().equals(toStationId)) {
                    toSchedule = schedule;
                }
            }

            if (fromSchedule != null && toSchedule != null
                    && fromSchedule.getSequenceNo() < toSchedule.getSequenceNo()) {
                Map<String, Object> result = new HashMap<>();
                result.put("trainId", entry.getKey());
                result.put("trainName", fromSchedule.getTrain().getName());
                result.put("departureTime", fromSchedule.getTimeOut());
                result.put("arrivalTime", toSchedule.getTimeIn());
                result.put("departureStation", fromSchedule.getStation().getName());
                result.put("arrivalStation", toSchedule.getStation().getName());
                result.put("date", date);
                results.add(result);
            }
        }

        if (results.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No trains found from " + fromStationId + " to " + toStationId + " on " + date);
        }

        return results;
    }

    @Override
    public List<Map<String, Object>> getTrainSchedule(String trainId) {
        if (!trainRepository.existsById(trainId)) {
            throw new ResourceNotFoundException("Train not found with ID: " + trainId);
        }

        List<Schedule> schedules = scheduleRepository.findByTrainIdOrderBySequenceNo(trainId);

        if (schedules.isEmpty()) {
            throw new ResourceNotFoundException("No schedule found for train ID: " + trainId);
        }

        return schedules.stream()
                .map(s -> {
                    Map<String, Object> station = new HashMap<>();
                    station.put("stationId", s.getStation().getStationId());
                    station.put("stationName", s.getStation().getName());
                    station.put("sequenceNo", s.getSequenceNo());
                    station.put("timeIn", s.getTimeIn());
                    station.put("timeOut", s.getTimeOut());
                    return station;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getTrainRoute(String trainId) {
        return getTrainSchedule(trainId);
    }

    @Override
    public Train createTrain(Train train) {
        if (trainRepository.existsById(train.getTrainId())) {
            throw new DuplicateResourceException("Train already exists with ID: " + train.getTrainId());
        }
        return trainRepository.save(train);
    }

    @Override
    public Train updateTrain(Train train) {
        if (!trainRepository.existsById(train.getTrainId())) {
            throw new ResourceNotFoundException("Train not found with ID: " + train.getTrainId());
        }
        return trainRepository.save(train);
    }

    @Override
    public void deleteTrain(String trainId) {
        if (!trainRepository.existsById(trainId)) {
            throw new ResourceNotFoundException("Train not found with ID: " + trainId);
        }
        trainRepository.deleteById(trainId);
    }
}