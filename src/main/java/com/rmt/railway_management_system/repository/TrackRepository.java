package com.rmt.railway_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rmt.railway_management_system.entity.Track;
import com.rmt.railway_management_system.entity.Track.TrackId;

@Repository
public interface TrackRepository extends JpaRepository<Track, TrackId> {
}