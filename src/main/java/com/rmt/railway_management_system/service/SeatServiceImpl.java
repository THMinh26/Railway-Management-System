package com.rmt.railway_management_system.service;

import com.rmt.railway_management_system.dto.SeatDTO;
import com.rmt.railway_management_system.entity.Seat;
import com.rmt.railway_management_system.repository.SeatRepository;
import com.rmt.railway_management_system.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SeatServiceImpl implements SeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public List<SeatDTO> getAvailableSeatsByCoachAndDate(String coachId, Date travelDate) {
        List<Seat> allSeats = seatRepository.findByCoachId(coachId);

        List<String> bookedSeatIds = ticketRepository.findBookedSeatIdsByCoachAndDate(coachId, travelDate);

        Set<String> bookedSeatSet = bookedSeatIds.stream().collect(Collectors.toSet());

        return allSeats.stream()
                .map(seat -> new SeatDTO(
                        seat.getSeatId(),
                        seat.getSeatNumber(),
                        !bookedSeatSet.contains(seat.getSeatId())))
                .collect(Collectors.toList());
    }
}