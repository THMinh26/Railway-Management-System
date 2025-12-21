package com.rmt.railway_management_system.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rmt.railway_management_system.dto.TrainSearchResponseDTO;
import com.rmt.railway_management_system.entity.Schedule;
import com.rmt.railway_management_system.entity.Train;
import com.rmt.railway_management_system.exception.ResourceNotFoundException;
import com.rmt.railway_management_system.repository.ScheduleRepository;
import com.rmt.railway_management_system.repository.TrainRepository;

@Service
public class TrainServiceImpl implements TrainService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TrainRepository trainRepository;

    @Override
    public List<TrainSearchResponseDTO> searchTrains(String source, String destination) {
        // Find trains that travel between source and destination
        List<Train> trains = scheduleRepository.findTrainsBetweenStations(source, destination);

        List<TrainSearchResponseDTO> results = new ArrayList<>();

        for (Train train : trains) {
            // Get schedule details for source and destination stations
            Schedule sourceSchedule = scheduleRepository.findByTrainIdAndStationName(train.getTrainId(), source);
            Schedule destSchedule = scheduleRepository.findByTrainIdAndStationName(train.getTrainId(), destination);

            if (sourceSchedule != null && destSchedule != null) {
                TrainSearchResponseDTO dto = new TrainSearchResponseDTO();
                dto.setId(train.getTrainId());
                dto.setTrainName(train.getTrainName());
                dto.setSource(source);
                dto.setDestination(destination);
                dto.setDepartureTime(sourceSchedule.getTimeOut());
                dto.setArrivalTime(destSchedule.getTimeIn());

                results.add(dto);
            }
        }

        return results;
    }

    @Override
    public Train findById(String trainId) {
        return trainRepository.findById(trainId)
                .orElseThrow(() -> new ResourceNotFoundException("Train not found with id: " + trainId));
    }
}