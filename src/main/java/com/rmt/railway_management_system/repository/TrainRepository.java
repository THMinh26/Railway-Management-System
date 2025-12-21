package com.rmt.railway_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rmt.railway_management_system.entity.Train;

@Repository
public interface TrainRepository extends JpaRepository<Train, String> {
    // Basic CRUD methods are inherited automatically
}