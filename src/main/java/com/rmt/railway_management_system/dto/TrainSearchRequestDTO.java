package com.rmt.railway_management_system.dto;

public class TrainSearchRequestDTO {
    private String source;
    private String destination;
    private String date;

    public TrainSearchRequestDTO() {
    }

    public TrainSearchRequestDTO(String source, String destination, String date) {
        this.source = source;
        this.destination = destination;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}