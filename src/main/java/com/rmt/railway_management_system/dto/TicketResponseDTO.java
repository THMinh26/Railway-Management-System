package com.rmt.railway_management_system.dto;

import java.sql.Date;

public class TicketResponseDTO {
    private Integer ticketId;
    private String seatNumber;
    private String coachName;
    private Date travelDate;
    private String startStationName;
    private String endStationName;
    private String passengerName;
    private String trainId;
    private String trainName;

    public TicketResponseDTO() {
    }

    public TicketResponseDTO(Integer ticketId, String seatNumber, String coachName,
            Date travelDate, String startStationName, String endStationName,
            String passengerName, String trainId, String trainName) {
        this.ticketId = ticketId;
        this.seatNumber = seatNumber;
        this.coachName = coachName;
        this.travelDate = travelDate;
        this.startStationName = startStationName;
        this.endStationName = endStationName;
        this.passengerName = passengerName;
        this.trainId = trainId;
        this.trainName = trainName;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public Date getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(Date travelDate) {
        this.travelDate = travelDate;
    }

    public String getStartStationName() {
        return startStationName;
    }

    public void setStartStationName(String startStationName) {
        this.startStationName = startStationName;
    }

    public String getEndStationName() {
        return endStationName;
    }

    public void setEndStationName(String endStationName) {
        this.endStationName = endStationName;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
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