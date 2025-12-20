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

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    // Constructors
    public Train() {
    }

    public Train(String trainId, String name) {
        this.trainId = trainId;
        this.name = name;
    }

    // Getters and Setters
    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Train{" +
                "trainId='" + trainId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}