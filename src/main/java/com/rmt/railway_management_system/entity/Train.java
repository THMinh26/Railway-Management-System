package com.rmt.railway_management_system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "train")
public class Train {

    @Id
    @Column(name = "train_id", length = 8, nullable = false)
    private String trainId;

    @Column(name = "train_name", nullable = false)
    private String trainName;

    public Train() {
    }

    public Train(String trainId, String trainName) {
        this.trainId = trainId;
        this.trainName = trainName;
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