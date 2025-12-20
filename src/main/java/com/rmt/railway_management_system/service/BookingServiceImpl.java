package com.rmt.railway_management_system.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rmt.railway_management_system.dto.BookingRequestDTO;
import com.rmt.railway_management_system.dto.TicketRequestDTO;
import com.rmt.railway_management_system.entity.Booking;
import com.rmt.railway_management_system.entity.Ticket;
import com.rmt.railway_management_system.entity.User;
import com.rmt.railway_management_system.exception.DuplicateResourceException;
import com.rmt.railway_management_system.exception.ResourceNotFoundException;
import com.rmt.railway_management_system.repository.BookingRepository;
import com.rmt.railway_management_system.repository.TicketRepository;
import com.rmt.railway_management_system.repository.UserRepository;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking getBookingById(String bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));
    }

    @Override
    public List<Booking> getBookingsByUsername(String username) {
        if (!userRepository.existsById(username)) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        return bookingRepository.findByUsername(username);
    }

    @Override
    public List<Ticket> getTicketsByBookingId(String bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new ResourceNotFoundException("Booking not found with ID: " + bookingId);
        }
        return ticketRepository.findByBookingId(bookingId);
    }

    @Override
    public Booking createBooking(Booking booking) {
        if (bookingRepository.existsById(booking.getBookingId())) {
            throw new DuplicateResourceException("Booking already exists with ID: " + booking.getBookingId());
        }
        if (!userRepository.existsById(booking.getUser().getUsername())) {
            throw new ResourceNotFoundException("User not found with username: " + booking.getUser().getUsername());
        }
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking createBookingWithTickets(BookingRequestDTO bookingRequest) {
        // Check if booking already exists
        if (bookingRepository.existsById(bookingRequest.getBookingId())) {
            throw new DuplicateResourceException("Booking already exists with ID: " + bookingRequest.getBookingId());
        }

        // Check if user exists
        User user = userRepository.findById(bookingRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with username: " + bookingRequest.getUsername()));

        // Create booking
        Booking booking = new Booking();
        booking.setBookingId(bookingRequest.getBookingId());
        booking.setBookingDate(Date.valueOf(bookingRequest.getBookingDate()));
        booking.setNumberOfTickets(bookingRequest.getNumberOfTickets());
        booking.setTotal(bookingRequest.getTotal());
        booking.setUser(user);

        Booking savedBooking = bookingRepository.save(booking);

        // Create tickets
        for (TicketRequestDTO ticketDTO : bookingRequest.getTickets()) {
            // Check if ticket already exists
            if (ticketRepository.existsById(ticketDTO.getTicketId())) {
                throw new DuplicateResourceException("Ticket already exists with ID: " + ticketDTO.getTicketId());
            }

            Ticket ticket = new Ticket();
            ticket.setTicketId(ticketDTO.getTicketId());
            ticket.setBooking(savedBooking);
            ticket.setSeatId(ticketDTO.getSeatId());
            ticket.setTravelDate(Date.valueOf(ticketDTO.getTravelDate()));
            ticket.setStartStationId(ticketDTO.getStartStationId());
            ticket.setEndStationId(ticketDTO.getEndStationId());
            ticket.setPassengerName(ticketDTO.getPassengerName());
            ticketRepository.save(ticket);
        }

        return savedBooking;
    }

    @Override
    public void deleteBooking(String bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new ResourceNotFoundException("Booking not found with ID: " + bookingId);
        }
        bookingRepository.deleteById(bookingId);
    }
}