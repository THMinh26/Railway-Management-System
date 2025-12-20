package com.rmt.railway_management_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rmt.railway_management_system.entity.Train;

@Repository
public interface TrainRepository extends JpaRepository<Train, String> {
    @Query("SELECT t FROM Train t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Train> searchByName(@Param("query") String query);
}