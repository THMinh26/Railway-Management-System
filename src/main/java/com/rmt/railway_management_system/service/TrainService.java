package com.rmt.railway_management_system.service;

import com.rmt.railway_management_system.dto.TrainSearchResponseDTO;
import com.rmt.railway_management_system.entity.Train;

import java.util.List;

public interface TrainService {
    List<TrainSearchResponseDTO> searchTrains(String source, String destination);

    Train findById(String trainId);
}