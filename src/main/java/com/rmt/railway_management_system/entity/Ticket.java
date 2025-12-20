package com.rmt.railway_management_system.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @Column(name = "ticket_id", length = 15, nullable = false)
    private String ticketId;

    @ManyToOne
    @JoinColumn(name = "booking_id", referencedColumnName = "booking_id", nullable = false)
    private Booking booking;

    @Column(name = "seat_id", length = 15, nullable = false)
    private String seatId;

    @Column(name = "travel_date", nullable = false)
    private Date travelDate;

    @Column(name = "start_station_id", length = 10, nullable = false)
    private String startStationId;

    @Column(name = "end_station_id", length = 10, nullable = false)
    private String endStationId;

    @Column(name = "passenger_name", length = 50, nullable = false)
    private String passengerName;

    // Constructors
    public Ticket() {
    }

    public Ticket(String ticketId, Booking booking, String seatId, Date travelDate,
            String startStationId, String endStationId, String passengerName) {
        this.ticketId = ticketId;
        this.booking = booking;
        this.seatId = seatId;
        this.travelDate = travelDate;
        this.startStationId = startStationId;
        this.endStationId = endStationId;
        this.passengerName = passengerName;
    }

    // Getters and Setters
    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public Date getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(Date travelDate) {
        this.travelDate = travelDate;
    }

    public String getStartStationId() {
        return startStationId;
    }

    public void setStartStationId(String startStationId) {
        this.startStationId = startStationId;
    }

    public String getEndStationId() {
        return endStationId;
    }

    public void setEndStationId(String endStationId) {
        this.endStationId = endStationId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId='" + ticketId + '\'' +
                ", booking=" + (booking != null ? booking.getBookingId() : null) +
                ", seatId='" + seatId + '\'' +
                ", travelDate=" + travelDate +
                ", startStationId='" + startStationId + '\'' +
                ", endStationId='" + endStationId + '\'' +
                ", passengerName='" + passengerName + '\'' +
                '}';
    }
}