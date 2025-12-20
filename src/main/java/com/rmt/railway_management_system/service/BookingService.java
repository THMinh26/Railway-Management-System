package com.rmt.railway_management_system.service;

import java.util.List;

import com.rmt.railway_management_system.dto.BookingRequestDTO;
import com.rmt.railway_management_system.entity.Booking;
import com.rmt.railway_management_system.entity.Ticket;

public interface BookingService {
    List<Booking> getAllBookings();

    Booking getBookingById(String bookingId);

    List<Booking> getBookingsByUsername(String username);

    List<Ticket> getTicketsByBookingId(String bookingId);

    Booking createBooking(Booking booking);

    Booking createBookingWithTickets(BookingRequestDTO bookingRequest);

    void deleteBooking(String bookingId);
}