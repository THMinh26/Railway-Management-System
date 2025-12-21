package com.rmt.railway_management_system.repository;

import com.rmt.railway_management_system.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, String> {

    @Query("SELECT s FROM Seat s WHERE s.coach.coachId = :coachId ORDER BY s.seatNumber")
    List<Seat> findByCoachId(@Param("coachId") String coachId);

    @Query("SELECT COUNT(s) FROM Seat s WHERE s.coach.coachId = :coachId")
    Long countByCoachId(@Param("coachId") String coachId);
}