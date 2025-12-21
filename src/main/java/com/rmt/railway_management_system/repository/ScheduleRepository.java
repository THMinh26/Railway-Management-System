package com.rmt.railway_management_system.repository;

import com.rmt.railway_management_system.entity.Schedule;
import com.rmt.railway_management_system.entity.Train;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Schedule.ScheduleId> {

        @Query("SELECT DISTINCT s.train FROM Schedule s WHERE " +
                        "s.train.trainId IN (" +
                        "  SELECT s1.train.trainId FROM Schedule s1 " +
                        "  WHERE s1.station.name = :source " +
                        "  AND EXISTS (" +
                        "    SELECT s2 FROM Schedule s2 " +
                        "    WHERE s2.train.trainId = s1.train.trainId " +
                        "    AND s2.station.name = :destination " +
                        "    AND s2.sequenceNo > s1.sequenceNo" +
                        "  )" +
                        ")")
        List<Train> findTrainsBetweenStations(
                        @Param("source") String source,
                        @Param("destination") String destination);

        @Query("SELECT s FROM Schedule s WHERE s.train.trainId = :trainId AND s.station.name = :stationName")
        Schedule findByTrainIdAndStationName(@Param("trainId") String trainId,
                        @Param("stationName") String stationName);
}