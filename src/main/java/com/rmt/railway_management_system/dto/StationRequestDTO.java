package com.rmt.railway_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class StationRequestDTO {
    
    @NotBlank(message = "Station ID is required")
    @Size(max = 10, message = "Station ID must not exceed 10 characters")
    private String stationId;
    
    @NotBlank(message = "Station name is required")
    @Size(max = 30, message = "Station name must not exceed 30 characters")
    private String name;

    public StationRequestDTO() {
    }

    public StationRequestDTO(String stationId, String name) {
        this.stationId = stationId;
        this.name = name;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
