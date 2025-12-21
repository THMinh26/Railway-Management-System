package com.rmt.railway_management_system.service;

import com.rmt.railway_management_system.dto.CoachDTO;
import com.rmt.railway_management_system.entity.Coach;
import com.rmt.railway_management_system.repository.CoachRepository;
import com.rmt.railway_management_system.repository.SeatRepository;
import com.rmt.railway_management_system.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoachServiceImpl implements CoachService {

    @Autowired
    private CoachRepository coachRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public List<CoachDTO> getCoachesByTrainId(String trainId, Date travelDate) {
        List<Coach> coaches = coachRepository.findByTrainId(trainId);

        return coaches.stream()
                .map(coach -> {
                    Long totalSeats = seatRepository.countByCoachId(coach.getCoachId());

                    List<String> bookedSeatIds = ticketRepository.findBookedSeatIdsByCoachAndDate(
                            coach.getCoachId(),
                            travelDate);

                    int availableSeats = totalSeats.intValue() - bookedSeatIds.size();

                    return new CoachDTO(
                            coach.getCoachId(),
                            coach.getCoachName(),
                            availableSeats);
                })
                .collect(Collectors.toList());
    }
}