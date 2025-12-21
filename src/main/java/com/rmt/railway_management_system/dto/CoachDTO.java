package com.rmt.railway_management_system.dto;

public class CoachDTO {
    private String coachId;
    private String coachName;
    private Integer availableSeats;

    public CoachDTO() {
    }

    public CoachDTO(String coachId, String coachName, Integer availableSeats) {
        this.coachId = coachId;
        this.coachName = coachName;
        this.availableSeats = availableSeats;
    }

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }
}