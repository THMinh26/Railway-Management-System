package com.rmt.railway_management_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rmt.railway_management_system.entity.Station;

@Repository
public interface StationRepository extends JpaRepository<Station, String> {
    @Query("SELECT s FROM Station s WHERE s.name = :name")
    Optional<Station> findByName(@Param("name") String name);
}