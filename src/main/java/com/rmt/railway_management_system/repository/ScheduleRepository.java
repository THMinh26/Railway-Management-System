package com.rmt.railway_management_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rmt.railway_management_system.entity.Schedule;
import com.rmt.railway_management_system.entity.Schedule.ScheduleId;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, ScheduleId> {
    @Query("SELECT s FROM Schedule s WHERE s.train.trainId = :trainId ORDER BY s.sequenceNo")
    List<Schedule> findByTrainIdOrderBySequenceNo(@Param("trainId") String trainId);
}