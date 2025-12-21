package com.rmt.railway_management_system.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ticket", schema = "public")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id", nullable = false)
    private Integer ticketId;

    @ManyToOne
    @JoinColumn(name = "booking_id", referencedColumnName = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "seat_id", referencedColumnName = "seat_id", nullable = false)
    private Seat seat;

    @Column(name = "travel_date", nullable = false)
    private Date travelDate;

    @ManyToOne
    @JoinColumn(name = "start_station_id", referencedColumnName = "station_id", nullable = false)
    private Station startStation;

    @ManyToOne
    @JoinColumn(name = "end_station_id", referencedColumnName = "station_id", nullable = false)
    private Station endStation;

    @Column(name = "passenger_name", length = 50, nullable = false)
    private String passengerName;

    public Ticket() {
    }

    public Ticket(Booking booking, Seat seat, Date travelDate,
            Station startStation, Station endStation, String passengerName) {
        this.booking = booking;
        this.seat = seat;
        this.travelDate = travelDate;
        this.startStation = startStation;
        this.endStation = endStation;
        this.passengerName = passengerName;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public Date getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(Date travelDate) {
        this.travelDate = travelDate;
    }

    public Station getStartStation() {
        return startStation;
    }

    public void setStartStation(Station startStation) {
        this.startStation = startStation;
    }

    public Station getEndStation() {
        return endStation;
    }

    public void setEndStation(Station endStation) {
        this.endStation = endStation;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }
}