package com.rmt.railway_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TrainRequestDTO {
    
    @NotBlank(message = "Train ID is required")
    @Size(max = 8, message = "Train ID must not exceed 8 characters")
    private String trainId;
    
    @NotBlank(message = "Train name is required")
    private String trainName;

    public TrainRequestDTO() {
    }

    public TrainRequestDTO(String trainId, String trainName) {
        this.trainId = trainId;
        this.trainName = trainName;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }
}
