package com.rmt.railway_management_system.dto;

import java.math.BigDecimal;
import java.util.List;

public class BookingRequestDTO {
    private String bookingId;
    private String bookingDate;
    private Integer numberOfTickets;
    private BigDecimal total;
    private String username;
    private List<TicketRequestDTO> tickets;

    public BookingRequestDTO() {
    }

    // Getters and Setters
    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Integer getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(Integer numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<TicketRequestDTO> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketRequestDTO> tickets) {
        this.tickets = tickets;
    }
}