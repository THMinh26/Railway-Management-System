package com.rmt.railway_management_system.dto;

import java.sql.Time;

public class TrainSearchResponseDTO {
    private String trainId;
    private String trainName;
    private String source;
    private String destination;
    private String departureTime;
    private String arrivalTime;

    public TrainSearchResponseDTO() {
    }

    public TrainSearchResponseDTO(String trainId, String trainName, String source,
            String destination, String departureTime, String arrivalTime, Integer availableSeats) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setId(String trainId) {
        this.trainId = trainId;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Time departureTime) {
        this.departureTime = departureTime.toString();
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Time arrivalTime) {
        this.arrivalTime = arrivalTime.toString();
    }
}