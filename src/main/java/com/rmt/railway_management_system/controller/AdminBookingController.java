package com.rmt.railway_management_system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rmt.railway_management_system.dto.ErrorResponseDTO;
import com.rmt.railway_management_system.entity.Booking;
import com.rmt.railway_management_system.repository.BookingRepository;
import com.rmt.railway_management_system.repository.TicketRepository;

@RestController
@RequestMapping("/api/admin/bookings")
@CrossOrigin(origins = "*")
public class AdminBookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TicketRepository ticketRepository;

    /**
     * UC20: View All Bookings
     * GET /api/admin/bookings
     */
    @GetMapping
    public ResponseEntity<?> getAllBookings() {
        try {
            List<Booking> bookings = bookingRepository.findAll();
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Bookings retrieved successfully");
            response.put("count", bookings.size());
            response.put("bookings", bookings);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO("Failed to retrieve bookings", e.getMessage()));
        }
    }

    /**
     * Get single booking by ID
     * GET /api/admin/bookings/{bookingId}
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<?> getBookingById(@PathVariable String bookingId) {
        try {
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Booking retrieved successfully");
            response.put("booking", booking);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDTO("Booking not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO("Failed to retrieve booking", e.getMessage()));
        }
    }

    /**
     * UC21: Delete Booking
     * DELETE /api/admin/bookings/{bookingId}
     */
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<?> deleteBooking(@PathVariable String bookingId) {
        try {
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));

            // Delete associated tickets first (query tickets by booking)
            ticketRepository.deleteAll(ticketRepository.findByBookingId(bookingId));
            
            // Then delete the booking
            bookingRepository.deleteById(bookingId);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Booking deleted successfully");
            response.put("bookingId", bookingId);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDTO("Booking not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO("Failed to delete booking", e.getMessage()));
        }
    }

    /**
     * Get bookings by user ID
     * GET /api/admin/bookings/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getBookingsByUserId(@PathVariable String userId) {
        try {
            List<Booking> bookings = bookingRepository.findByUserId(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Bookings retrieved successfully");
            response.put("count", bookings.size());
            response.put("bookings", bookings);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO("Failed to retrieve bookings", e.getMessage()));
        }
    }
}
