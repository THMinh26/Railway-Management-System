package com.rmt.railway_management_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rmt.railway_management_system.service.TrainService;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/booking")
public class BookingController {

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
}