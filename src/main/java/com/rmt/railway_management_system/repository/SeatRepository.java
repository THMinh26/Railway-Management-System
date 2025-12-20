package com.rmt.railway_management_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rmt.railway_management_system.entity.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, String> {
    @Query("SELECT s FROM Seat s WHERE s.coach.coachId = :coachId")
    List<Seat> findByCoachId(@Param("coachId") String coachId);
}