package com.rmt.railway_management_system.service;

import java.util.List;

import com.rmt.railway_management_system.dto.BookingRequestDTO;
import com.rmt.railway_management_system.dto.BookingResponseDTO;
import com.rmt.railway_management_system.entity.Booking;

public interface BookingService {
    List<BookingResponseDTO> getUserBookings(Integer userId);

    BookingResponseDTO getBookingDetails(String bookingId);

    void cancelBooking(String bookingId) throws Exception;

    BookingResponseDTO createBooking(BookingRequestDTO request) throws Exception;
    
    BookingResponseDTO mapToBookingResponseDTO(Booking booking);
}