package com.rmt.railway_management_system.service;

import com.rmt.railway_management_system.dto.SeatDTO;
import java.sql.Date;
import java.util.List;

public interface SeatService {
    List<SeatDTO> getAvailableSeatsByCoachAndDate(String coachId, Date travelDate);
}