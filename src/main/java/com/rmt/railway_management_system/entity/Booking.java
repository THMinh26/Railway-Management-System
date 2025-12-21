package com.rmt.railway_management_system.entity;

import java.math.BigDecimal;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @Column(name = "booking_id", length = 10, nullable = false)
    private String bookingId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @Column(name = "booking_date", nullable = false)
    private Date bookingDate;

    @Column(name = "number_of_tickets", nullable = false)
    private int numberOfTickets;

    @Column(name = "total", precision = 10, scale = 2, nullable = false)
    private BigDecimal total;

    public Booking() {
    }

    public Booking(String bookingId, User user, Date bookingDate, int numberOfTickets, BigDecimal total) {
        this.bookingId = bookingId;
        this.user = user;
        this.bookingDate = bookingDate;
        this.numberOfTickets = numberOfTickets;
        this.total = total;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}