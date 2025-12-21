package com.rmt.railway_management_system.dto;

public class ScheduleResponseDTO {
    
    private String trainId;
    private String trainName;
    private String stationId;
    private String stationName;
    private Integer sequenceNo;
    private String timeIn;
    private String timeOut;

    public ScheduleResponseDTO() {
    }

    public ScheduleResponseDTO(String trainId, String trainName, String stationId, String stationName, 
                               Integer sequenceNo, String timeIn, String timeOut) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.stationId = stationId;
        this.stationName = stationName;
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

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
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
