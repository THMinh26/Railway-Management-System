package com.rmt.railway_management_system.service;

import com.rmt.railway_management_system.dto.CoachDTO;

import java.sql.Date;
import java.util.List;

public interface CoachService {
    List<CoachDTO> getCoachesByTrainId(String trainId, Date travelDate);
}