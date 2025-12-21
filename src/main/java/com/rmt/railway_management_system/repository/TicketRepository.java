package com.rmt.railway_management_system.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rmt.railway_management_system.entity.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    @Query("SELECT t.seat.seatId FROM Ticket t WHERE t.seat.coach.coachId = :coachId AND t.travelDate = :travelDate")
    List<String> findBookedSeatIdsByCoachAndDate(@Param("coachId") String coachId,
            @Param("travelDate") Date travelDate);

    @Query("SELECT t FROM Ticket t WHERE t.booking.bookingId = :bookingId")
    List<Ticket> findByBookingId(@Param("bookingId") String bookingId);
}