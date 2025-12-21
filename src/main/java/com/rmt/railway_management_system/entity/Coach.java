package com.rmt.railway_management_system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "coach", uniqueConstraints = @UniqueConstraint(name = "coach_train_id_coach_number_key", columnNames = {
        "train_id", "coach_name" }))
public class Coach {

    @Id
    @Column(name = "coach_id", length = 8, nullable = false)
    private String coachId;

    @ManyToOne
    @JoinColumn(name = "train_id", referencedColumnName = "train_id", nullable = false)
    private Train train;

    @Column(name = "coach_name", length = 10, nullable = false)
    private String coachName;

    public Coach() {
    }

    public Coach(String coachId, Train train, String coachName) {
        this.coachId = coachId;
        this.train = train;
        this.coachName = coachName;
    }

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

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }
}