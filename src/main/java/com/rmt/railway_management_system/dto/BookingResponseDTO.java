package com.rmt.railway_management_system.dto;

import java.sql.Date;
import java.util.List;

public class BookingResponseDTO {
    private String bookingId;
    private int userId;
    private String username;
    private Date bookingDate;
    private int numberOfTickets;
    private Long total;
    private String status;
    private List<TicketResponseDTO> tickets;

    public BookingResponseDTO() {
    }

    public BookingResponseDTO(String bookingId, int userId, String username, Date bookingDate,
            int numberOfTickets, Long total, String status,
            List<TicketResponseDTO> tickets) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.username = username;
        this.bookingDate = bookingDate;
        this.numberOfTickets = numberOfTickets;
        this.total = total;
        this.status = status;
        this.tickets = tickets;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TicketResponseDTO> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketResponseDTO> tickets) {
        this.tickets = tickets;
    }
}