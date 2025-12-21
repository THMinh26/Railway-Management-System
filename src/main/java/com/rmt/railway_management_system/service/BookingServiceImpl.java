package com.rmt.railway_management_system.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rmt.railway_management_system.dto.BookingRequestDTO;
import com.rmt.railway_management_system.dto.BookingResponseDTO;
import com.rmt.railway_management_system.dto.TicketResponseDTO;
import com.rmt.railway_management_system.entity.Booking;
import com.rmt.railway_management_system.entity.Seat;
import com.rmt.railway_management_system.entity.Station;
import com.rmt.railway_management_system.entity.Ticket;
import com.rmt.railway_management_system.entity.User;
import com.rmt.railway_management_system.repository.BookingRepository;
import com.rmt.railway_management_system.repository.SeatRepository;
import com.rmt.railway_management_system.repository.StationRepository;
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

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private StationRepository stationRepository;

    @Override
    public List<BookingResponseDTO> getUserBookings(Integer userId) {
        List<Booking> bookings = bookingRepository.findByUserId(userId);

        return bookings.stream()
                .map(this::mapToBookingResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponseDTO getBookingDetails(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        return mapToBookingResponseDTO(booking);
    }

    @Override
    @Transactional
    public void cancelBooking(String bookingId) throws Exception {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if ("CANCELLED".equals(booking.getStatus())) {
            throw new Exception("Booking is already cancelled");
        }

        List<Ticket> tickets = ticketRepository.findByBookingId(bookingId);

        if (tickets.isEmpty()) {
            throw new Exception("No tickets found for this booking");
        }

        for (int i = 0; i < tickets.size(); i++) {
            ticketRepository.delete(tickets.get(i));
        }
        Date travelDate = tickets.get(0).getTravelDate();
        LocalDate travel = travelDate.toLocalDate();
        LocalDate today = LocalDate.now();

        if (travel.minusDays(1).isBefore(today) || travel.minusDays(1).isEqual(today)) {
            throw new Exception("Cannot cancel booking. Cancellation must be done at least 1 day before travel date.");
        }

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO request) throws Exception {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new Exception("User not found"));

        Station startStation = stationRepository.findByName(request.getStartStationName())
                .orElseThrow(() -> new Exception("Start station not found: " + request.getStartStationName()));

        Station endStation = stationRepository.findByName(request.getEndStationName())
                .orElseThrow(() -> new Exception("End station not found: " + request.getEndStationName()));

        String bookingId = "BK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Date bookingDate = Date.valueOf(LocalDate.now());
        Date travelDate = Date.valueOf(request.getTravelDate());

        Booking booking = new Booking();
        booking.setBookingId(bookingId);
        booking.setUser(user);
        booking.setBookingDate(bookingDate);
        booking.setNumberOfTickets(request.getNumberOfTickets());
        booking.setTotal(request.getTotal());
        booking.setStatus("CONFIRMED");

        bookingRepository.save(booking);

        List<Ticket> tickets = new ArrayList<>();
        for (BookingRequestDTO.PassengerDTO passenger : request.getPassengers()) {
            Seat seat = seatRepository.findById(passenger.getSeatId())
                    .orElseThrow(() -> new Exception("Seat not found: " + passenger.getSeatId()));

            // Check if seat is already booked
            List<String> bookedSeats = ticketRepository.findBookedSeatIdsByCoachAndDate(
                    seat.getCoach().getCoachId(),
                    travelDate);

            if (bookedSeats.contains(seat.getSeatId())) {
                throw new Exception("Seat " + seat.getSeatNumber() + " is already booked");
            }

            // Don't set ticket ID - it will be auto-generated
            Ticket ticket = new Ticket();
            ticket.setBooking(booking);
            ticket.setSeat(seat);
            ticket.setTravelDate(travelDate);
            ticket.setStartStation(startStation);
            ticket.setEndStation(endStation);
            ticket.setPassengerName(passenger.getPassengerName());

            ticketRepository.save(ticket);
            tickets.add(ticket);
        }

        return mapToBookingResponseDTO(booking);
    }

    public BookingResponseDTO mapToBookingResponseDTO(Booking booking) {
        List<Ticket> tickets = ticketRepository.findByBookingId(booking.getBookingId());

        List<TicketResponseDTO> ticketDTOs = tickets.stream()
                .map(ticket -> new TicketResponseDTO(
                        ticket.getTicketId(),
                        ticket.getSeat().getSeatNumber(),
                        ticket.getSeat().getCoach().getCoachName(),
                        ticket.getTravelDate(),
                        ticket.getStartStation().getName(),
                        ticket.getEndStation().getName(),
                        ticket.getPassengerName(),
                        ticket.getSeat().getCoach().getTrain().getTrainId(),
                        "Train " + ticket.getSeat().getCoach().getTrain().getTrainName()))
                .collect(Collectors.toList());

        return new BookingResponseDTO(
                booking.getBookingId(),
                booking.getUser().getUserId(),
                booking.getUser().getUsername(),
                booking.getBookingDate(),
                booking.getNumberOfTickets(),
                booking.getTotal(),
                booking.getStatus(),
                ticketDTOs);
    }
}