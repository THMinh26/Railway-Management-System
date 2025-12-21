package com.rmt.railway_management_system.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rmt.railway_management_system.dto.BookingRequestDTO;
import com.rmt.railway_management_system.dto.BookingResponseDTO;
import com.rmt.railway_management_system.service.BookingService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/booking")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public void showBookingPage(
            @RequestParam String trainId,
            @RequestParam String trainName,
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam String date,
            @RequestParam String departureTime,
            @RequestParam String arrivalTime,
            HttpServletResponse response) throws IOException {

        response.sendRedirect("/booking.html?" +
                "trainId=" + trainId +
                "&trainName=" + trainName +
                "&source=" + source +
                "&destination=" + destination +
                "&date=" + date +
                "&departureTime=" + departureTime +
                "&arrivalTime=" + arrivalTime);
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequestDTO request) {
        try {
            BookingResponseDTO booking = bookingService.createBooking(request);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingResponseDTO>> getUserBookings(@PathVariable String userId) {
        List<BookingResponseDTO> bookings = bookingService.getUserBookings(userId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponseDTO> getBookingDetails(@PathVariable String bookingId) {
        try {
            BookingResponseDTO booking = bookingService.getBookingDetails(bookingId);
            return ResponseEntity.ok(booking);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<String> cancelBooking(@PathVariable String bookingId) {
        try {
            bookingService.cancelBooking(bookingId);
            return ResponseEntity.ok("Booking cancelled successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}