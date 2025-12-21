package com.rmt.railway_management_system.dto;

public class SeatDTO {
    private String seatId;
    private String seatNumber;
    private boolean available;

    public SeatDTO() {
    }

    public SeatDTO(String seatId, String seatNumber, boolean available) {
        this.seatId = seatId;
        this.seatNumber = seatNumber;
        this.available = available;
    }

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}