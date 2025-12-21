package com.rmt.railway_management_system.entity;

import java.io.Serializable;
import java.sql.Time;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "schedule", uniqueConstraints = {
        @UniqueConstraint(name = "schedule_train_id_sequence_no_key", columnNames = { "train_id", "sequence_no" })
})
public class Schedule {

    @Embeddable
    public static class ScheduleId implements Serializable {
        @Column(name = "train_id", length = 8, nullable = false)
        private String trainId;

        @Column(name = "station_id", length = 10, nullable = false)
        private String stationId;

        public ScheduleId() {
        }

        public ScheduleId(String trainId, String stationId) {
            this.trainId = trainId;
            this.stationId = stationId;
        }

        public String getTrainId() {
            return trainId;
        }

        public void setTrainId(String trainId) {
            this.trainId = trainId;
        }

        public String getStationId() {
            return stationId;
        }

        public void setStationId(String stationId) {
            this.stationId = stationId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof ScheduleId))
                return false;
            ScheduleId that = (ScheduleId) o;
            return Objects.equals(trainId, that.trainId) &&
                    Objects.equals(stationId, that.stationId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(trainId, stationId);
        }
    }

    @EmbeddedId
    private ScheduleId id;

    @ManyToOne
    @MapsId("trainId")
    @JoinColumn(name = "train_id", referencedColumnName = "train_id", nullable = false)
    private Train train;

    @ManyToOne
    @MapsId("stationId")
    @JoinColumn(name = "station_id", referencedColumnName = "station_id", nullable = false)
    private Station station;

    @Column(name = "sequence_no", nullable = false)
    private int sequenceNo;

    @Column(name = "time_in")
    private Time timeIn;

    @Column(name = "time_out")
    private Time timeOut;

    public Schedule() {
    }

    public Schedule(Train train, Station station, int sequenceNo, Time timeIn, Time timeOut) {
        this.train = train;
        this.station = station;
        this.sequenceNo = sequenceNo;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.id = new ScheduleId(train.getTrainId(), station.getStationId());
    }

    public ScheduleId getId() {
        return id;
    }

    public void setId(ScheduleId id) {
        this.id = id;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
        if (this.id == null) {
            this.id = new ScheduleId();
        }
        this.id.setTrainId(train != null ? train.getTrainId() : null);
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
        if (this.id == null) {
            this.id = new ScheduleId();
        }
        this.id.setStationId(station != null ? station.getStationId() : null);
    }

    public int getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public Time getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(Time timeIn) {
        this.timeIn = timeIn;
    }

    public Time getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Time timeOut) {
        this.timeOut = timeOut;
    }
}