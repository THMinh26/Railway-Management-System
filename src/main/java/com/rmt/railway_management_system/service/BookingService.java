package com.rmt.railway_management_system.service;

import java.util.List;

import com.rmt.railway_management_system.dto.BookingRequestDTO;
import com.rmt.railway_management_system.dto.BookingResponseDTO;

public interface BookingService {
    List<BookingResponseDTO> getUserBookings(String userId);

    BookingResponseDTO getBookingDetails(String bookingId);

    void cancelBooking(String bookingId) throws Exception;

    BookingResponseDTO createBooking(BookingRequestDTO request) throws Exception;
}