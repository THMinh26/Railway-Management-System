package com.rmt.railway_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ScheduleRequestDTO {
    
    @NotBlank(message = "Train ID is required")
    private String trainId;
    
    @NotBlank(message = "Station ID is required")
    private String stationId;
    
    @NotNull(message = "Sequence number is required")
    private Integer sequenceNo;
    
    @NotBlank(message = "Time in is required (format: HH:mm:ss)")
    private String timeIn;
    
    @NotBlank(message = "Time out is required (format: HH:mm:ss)")
    private String timeOut;

    public ScheduleRequestDTO() {
    }

    public ScheduleRequestDTO(String trainId, String stationId, Integer sequenceNo, String timeIn, String timeOut) {
        this.trainId = trainId;
        this.stationId = stationId;
        this.sequenceNo = sequenceNo;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }
}
