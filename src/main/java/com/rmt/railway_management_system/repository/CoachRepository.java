package com.rmt.railway_management_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rmt.railway_management_system.entity.Coach;

@Repository
public interface CoachRepository extends JpaRepository<Coach, String> {
    @Query("SELECT c FROM Coach c WHERE c.train.trainId = :trainId")
    List<Coach> findByTrainId(@Param("trainId") String trainId);
}