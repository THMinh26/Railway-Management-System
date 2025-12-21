package com.rmt.railway_management_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rmt.railway_management_system.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    @Query("SELECT b FROM Booking b WHERE b.user.userId = :userId ORDER BY b.bookingDate DESC")
    List<Booking> findByUserId(@Param("userId") String userId);
}