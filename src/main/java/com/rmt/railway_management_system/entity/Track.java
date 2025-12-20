package com.rmt.railway_management_system.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "track")
public class Track {

    @Embeddable
    public static class TrackId implements Serializable {
        @Column(name = "start_station_id", length = 10, nullable = false)
        private String startStationId;

        @Column(name = "end_station_id", length = 10, nullable = false)
        private String endStationId;

        public TrackId() {
        }

        public TrackId(String startStationId, String endStationId) {
            this.startStationId = startStationId;
            this.endStationId = endStationId;
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

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof TrackId))
                return false;
            TrackId trackId = (TrackId) o;
            return Objects.equals(startStationId, trackId.startStationId) &&
                    Objects.equals(endStationId, trackId.endStationId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(startStationId, endStationId);
        }
    }

    @EmbeddedId
    private TrackId id;

    @ManyToOne
    @MapsId("startStationId")
    @JoinColumn(name = "start_station_id", referencedColumnName = "station_id", nullable = false)
    private Station startStation;

    @ManyToOne
    @MapsId("endStationId")
    @JoinColumn(name = "end_station_id", referencedColumnName = "station_id", nullable = false)
    private Station endStation;

    // Constructors
    public Track() {
    }

    public Track(Station startStation, Station endStation) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.id = new TrackId(startStation.getStationId(), endStation.getStationId());
    }

    public TrackId getId() {
        return id;
    }

    public void setId(TrackId id) {
        this.id = id;
    }

    public Station getStartStation() {
        return startStation;
    }

    public void setStartStation(Station startStation) {
        this.startStation = startStation;
        if (this.id == null) {
            this.id = new TrackId();
        }
        this.id.setStartStationId(startStation != null ? startStation.getStationId() : null);
    }

    public Station getEndStation() {
        return endStation;
    }

    public void setEndStation(Station endStation) {
        this.endStation = endStation;
        if (this.id == null) {
            this.id = new TrackId();
        }
        this.id.setEndStationId(endStation != null ? endStation.getStationId() : null);
    }

    @Override
    public String toString() {
        return "Track{" +
                "startStation=" + (startStation != null ? startStation.getStationId() : null) +
                ", endStation=" + (endStation != null ? endStation.getStationId() : null) +
                '}';
    }
}