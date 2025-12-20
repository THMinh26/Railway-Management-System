package com.rmt.railway_management_system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "station")
public class Station {

    @Id
    @Column(name = "station_id", length = 10, nullable = false)
    private String stationId;

    @Column(name = "name", length = 30, nullable = false, unique = true)
    private String name;

    // Constructors
    public Station() {
    }

    public Station(String stationId, String name) {
        this.stationId = stationId;
        this.name = name;
    }

    // Getters and Setters
    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Station{" +
                "stationId='" + stationId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}