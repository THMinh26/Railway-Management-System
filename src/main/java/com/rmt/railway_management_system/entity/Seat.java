package com.rmt.railway_management_system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "seat", uniqueConstraints = @UniqueConstraint(columnNames = { "coach_id", "seat_number" }))
public class Seat {

    @Id
    @Column(name = "seat_id", length = 15, nullable = false)
    private String seatId;

    @ManyToOne
    @JoinColumn(name = "coach_id", referencedColumnName = "coach_id", nullable = false)
    private Coach coach;

    @Column(name = "seat_number", length = 5, nullable = false)
    private String seatNumber;

    public Seat() {
    }

    public Seat(String seatId, Coach coach, String seatNumber) {
        this.seatId = seatId;
        this.coach = coach;
        this.seatNumber = seatNumber;
    }

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
}