package com.rmt.railway_management_system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "coach", uniqueConstraints = @UniqueConstraint(columnNames = { "train_id", "coach_number" }))
public class Coach {

    @Id
    @Column(name = "coach_id", length = 8, nullable = false)
    private String coachId;

    @ManyToOne
    @JoinColumn(name = "train_id", referencedColumnName = "train_id")
    private Train train;

    @Column(name = "coach_number", length = 10, nullable = false)
    private String coachNumber;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "type_id")
    private SeatType seatType;

    // Constructors
    public Coach() {
    }

    public Coach(String coachId, Train train, String coachNumber, SeatType seatType) {
        this.coachId = coachId;
        this.train = train;
        this.coachNumber = coachNumber;
        this.seatType = seatType;
    }

    // Getters and Setters
    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public String getCoachNumber() {
        return coachNumber;
    }

    public void setCoachNumber(String coachNumber) {
        this.coachNumber = coachNumber;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }

    @Override
    public String toString() {
        return "Coach{" +
                "coachId='" + coachId + '\'' +
                ", train=" + (train != null ? train.getTrainId() : null) +
                ", coachNumber='" + coachNumber + '\'' +
                ", seatType=" + (seatType != null ? seatType.getTypeId() : null) +
                '}';
    }
}