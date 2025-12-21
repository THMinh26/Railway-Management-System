package com.rmt.railway_management_system.config;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.rmt.railway_management_system.entity.Booking;
import com.rmt.railway_management_system.entity.Coach;
import com.rmt.railway_management_system.entity.Schedule;
import com.rmt.railway_management_system.entity.Seat;
import com.rmt.railway_management_system.entity.Station;
import com.rmt.railway_management_system.entity.Ticket;
import com.rmt.railway_management_system.entity.Train;
import com.rmt.railway_management_system.entity.User;
import com.rmt.railway_management_system.repository.BookingRepository;
import com.rmt.railway_management_system.repository.CoachRepository;
import com.rmt.railway_management_system.repository.ScheduleRepository;
import com.rmt.railway_management_system.repository.SeatRepository;
import com.rmt.railway_management_system.repository.StationRepository;
import com.rmt.railway_management_system.repository.TicketRepository;
import com.rmt.railway_management_system.repository.TrainRepository;
import com.rmt.railway_management_system.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CoachRepository coachRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TicketRepository ticketRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) throws Exception {
        // Only initialize if database is empty
        if (stationRepository.count() > 0) {
            System.out.println("Data already exists. Skipping initialization.");
            return;
        }

        System.out.println("Initializing sample data...");

        // 1. Create Stations
        createStations();

        // 2. Create Trains
        createTrains();

        // 3. Create Schedules
        createSchedules();

        // 4. Create Users (including admin)
        createUsers();

        // 5. Create Coaches
        createCoaches();

        // 6. Create Seats
        createSeats();

        // 7. Create Sample Bookings
        createBookings();

        System.out.println("Sample data initialization completed!");
    }

    private void createStations() {
        List<Station> stations = new ArrayList<>();
        stations.add(new Station("ST001", "Hanoi"));
        stations.add(new Station("ST002", "Hai Phong"));
        stations.add(new Station("ST003", "Nam Dinh"));
        stations.add(new Station("ST004", "Thanh Hoa"));
        stations.add(new Station("ST005", "Vinh"));
        stations.add(new Station("ST006", "Dong Hoi"));
        stations.add(new Station("ST007", "Hue"));
        stations.add(new Station("ST008", "Da Nang"));
        stations.add(new Station("ST009", "Quang Ngai"));
        stations.add(new Station("ST010", "Quy Nhon"));
        stations.add(new Station("ST011", "Nha Trang"));
        stations.add(new Station("ST012", "Phan Thiet"));
        stations.add(new Station("ST013", "Bien Hoa"));
        stations.add(new Station("ST014", "Ho Chi Minh City"));
        stations.add(new Station("ST015", "Can Tho"));

        stationRepository.saveAll(stations);
        System.out.println("Created " + stations.size() + " stations");
    }

    private void createTrains() {
        List<Train> trains = new ArrayList<>();
        trains.add(new Train("SE1", "Reunification Express SE1"));
        trains.add(new Train("SE2", "Reunification Express SE2"));
        trains.add(new Train("SE3", "Reunification Express SE3"));
        trains.add(new Train("SE4", "Reunification Express SE4"));
        trains.add(new Train("SE5", "Reunification Express SE5"));
        trains.add(new Train("SE6", "Reunification Express SE6"));
        trains.add(new Train("SE7", "Reunification Express SE7"));
        trains.add(new Train("SE8", "Reunification Express SE8"));
        trains.add(new Train("SPT1", "Sai Gon - Phan Thiet Express"));
        trains.add(new Train("SNT1", "Sai Gon - Nha Trang Express"));

        trainRepository.saveAll(trains);
        System.out.println("Created " + trains.size() + " trains");
    }

    private void createSchedules() {
        // SE1: Hanoi -> Ho Chi Minh City (Full route)
        createTrainSchedule("SE1", new String[][]{
            {"ST001", "1", "00:00:00", "06:00:00"},  // Hanoi
            {"ST004", "2", "09:30:00", "09:45:00"},  // Thanh Hoa
            {"ST005", "3", "12:15:00", "12:30:00"},  // Vinh
            {"ST007", "4", "18:00:00", "18:20:00"},  // Hue
            {"ST008", "5", "20:30:00", "20:45:00"},  // Da Nang
            {"ST011", "6", "03:30:00", "03:50:00"},  // Nha Trang
            {"ST014", "7", "10:00:00", "10:00:00"}   // Ho Chi Minh City
        });

        // SE2: Ho Chi Minh City -> Hanoi (Reverse)
        createTrainSchedule("SE2", new String[][]{
            {"ST014", "1", "00:00:00", "19:00:00"},  // Ho Chi Minh City
            {"ST011", "2", "01:30:00", "01:50:00"},  // Nha Trang
            {"ST008", "3", "08:30:00", "08:45:00"},  // Da Nang
            {"ST007", "4", "11:00:00", "11:20:00"},  // Hue
            {"ST005", "5", "16:45:00", "17:00:00"},  // Vinh
            {"ST004", "6", "19:45:00", "20:00:00"},  // Thanh Hoa
            {"ST001", "7", "23:30:00", "23:30:00"}   // Hanoi
        });

        // SE3: Hanoi -> Ho Chi Minh City (Alternative timing)
        createTrainSchedule("SE3", new String[][]{
            {"ST001", "1", "00:00:00", "08:00:00"},  // Hanoi
            {"ST004", "2", "11:30:00", "11:45:00"},  // Thanh Hoa
            {"ST005", "3", "14:15:00", "14:30:00"},  // Vinh
            {"ST007", "4", "20:00:00", "20:20:00"},  // Hue
            {"ST008", "5", "22:30:00", "22:45:00"},  // Da Nang
            {"ST011", "6", "05:30:00", "05:50:00"},  // Nha Trang
            {"ST014", "7", "12:00:00", "12:00:00"}   // Ho Chi Minh City
        });

        // SE4: Ho Chi Minh City -> Hanoi
        createTrainSchedule("SE4", new String[][]{
            {"ST014", "1", "00:00:00", "20:00:00"},
            {"ST011", "2", "02:30:00", "02:50:00"},
            {"ST008", "3", "09:30:00", "09:45:00"},
            {"ST007", "4", "12:00:00", "12:20:00"},
            {"ST005", "5", "17:45:00", "18:00:00"},
            {"ST004", "6", "20:45:00", "21:00:00"},
            {"ST001", "7", "00:30:00", "00:30:00"}
        });

        // SE5: Hanoi -> Da Nang (Express)
        createTrainSchedule("SE5", new String[][]{
            {"ST001", "1", "00:00:00", "07:00:00"},  // Hanoi
            {"ST004", "2", "10:15:00", "10:30:00"},  // Thanh Hoa
            {"ST005", "3", "13:00:00", "13:15:00"},  // Vinh
            {"ST007", "4", "18:30:00", "18:50:00"},  // Hue
            {"ST008", "5", "21:00:00", "21:00:00"}   // Da Nang
        });

        // SE6: Da Nang -> Hanoi (Express)
        createTrainSchedule("SE6", new String[][]{
            {"ST008", "1", "00:00:00", "06:00:00"},  // Da Nang
            {"ST007", "2", "08:15:00", "08:35:00"},  // Hue
            {"ST005", "3", "13:00:00", "13:15:00"},  // Vinh
            {"ST004", "4", "15:45:00", "16:00:00"},  // Thanh Hoa
            {"ST001", "5", "19:30:00", "19:30:00"}   // Hanoi
        });

        // SE7: Hanoi -> Nha Trang
        createTrainSchedule("SE7", new String[][]{
            {"ST001", "1", "00:00:00", "05:30:00"},  // Hanoi
            {"ST005", "2", "11:45:00", "12:00:00"},  // Vinh
            {"ST007", "3", "17:30:00", "17:50:00"},  // Hue
            {"ST008", "4", "20:00:00", "20:15:00"},  // Da Nang
            {"ST011", "5", "03:00:00", "03:00:00"}   // Nha Trang
        });

        // SE8: Nha Trang -> Hanoi
        createTrainSchedule("SE8", new String[][]{
            {"ST011", "1", "00:00:00", "18:00:00"},  // Nha Trang
            {"ST008", "2", "00:45:00", "01:00:00"},  // Da Nang
            {"ST007", "3", "03:15:00", "03:35:00"},  // Hue
            {"ST005", "4", "08:30:00", "08:45:00"},  // Vinh
            {"ST001", "5", "15:30:00", "15:30:00"}   // Hanoi
        });

        // SPT1: Ho Chi Minh City -> Phan Thiet
        createTrainSchedule("SPT1", new String[][]{
            {"ST014", "1", "00:00:00", "06:00:00"},  // Ho Chi Minh City
            {"ST013", "2", "06:45:00", "07:00:00"},  // Bien Hoa
            {"ST012", "3", "10:00:00", "10:00:00"}   // Phan Thiet
        });

        // SNT1: Ho Chi Minh City -> Nha Trang
        createTrainSchedule("SNT1", new String[][]{
            {"ST014", "1", "00:00:00", "22:00:00"},  // Ho Chi Minh City
            {"ST012", "2", "01:00:00", "01:15:00"},  // Phan Thiet
            {"ST011", "3", "07:00:00", "07:00:00"}   // Nha Trang
        });

        System.out.println("Created schedules for all trains");
    }

    private void createTrainSchedule(String trainId, String[][] scheduleData) {
        Train train = trainRepository.findById(trainId).orElseThrow();
        for (String[] data : scheduleData) {
            Station station = stationRepository.findById(data[0]).orElseThrow();
            Schedule schedule = new Schedule(
                train,
                station,
                Integer.parseInt(data[1]),
                Time.valueOf(data[2]),
                Time.valueOf(data[3])
            );
            scheduleRepository.save(schedule);
        }
    }

    private void createUsers() {
        List<User> users = new ArrayList<>();
        
        // Admin user
        users.add(new User(
            "Administrator",
            "admin@railway.com",
            passwordEncoder.encode("admin123"),
            "0901234567",
            "ADMIN",
            "admin"
        ));

        // Regular users
        users.add(new User(
            "Nguyen Van An",
            "nguyenvanan@gmail.com",
            passwordEncoder.encode("password123"),
            "0912345678",
            "USER",
            "vanan"
        ));

        users.add(new User(
            "Tran Thi Binh",
            "tranthibinh@gmail.com",
            passwordEncoder.encode("password123"),
            "0923456789",
            "USER",
            "thibinh"
        ));

        users.add(new User(
            "Le Van Cuong",
            "levancuong@gmail.com",
            passwordEncoder.encode("password123"),
            "0934567890",
            "USER",
            "vancuong"
        ));

        users.add(new User(
            "Pham Thi Dung",
            "phamthidung@gmail.com",
            passwordEncoder.encode("password123"),
            "0945678901",
            "USER",
            "thidung"
        ));

        users.add(new User(
            "Hoang Van Em",
            "hoangvanem@gmail.com",
            passwordEncoder.encode("password123"),
            "0956789012",
            "USER",
            "vanem"
        ));

        users.add(new User(
            "Do Thi Phuong",
            "dothiphuong@gmail.com",
            passwordEncoder.encode("password123"),
            "0967890123",
            "USER",
            "thiphuong"
        ));

        users.add(new User(
            "Nguyen Van Giang",
            "nguyenvangiang@gmail.com",
            passwordEncoder.encode("password123"),
            "0978901234",
            "USER",
            "vangiang"
        ));

        userRepository.saveAll(users);
        System.out.println("Created " + users.size() + " users (1 admin, " + (users.size() - 1) + " regular)");
    }

    private void createCoaches() {
        List<Coach> coaches = new ArrayList<>();
        List<Train> trains = trainRepository.findAll();

        for (Train train : trains) {
            // Each train has 8 coaches
            for (int i = 1; i <= 8; i++) {
                String coachName = "Coach " + i;
                String coachId = train.getTrainId() + "_C" + i;
                coaches.add(new Coach(coachId, train, coachName));
            }
        }

        coachRepository.saveAll(coaches);
        System.out.println("Created " + coaches.size() + " coaches");
    }

    private void createSeats() {
        List<Seat> seats = new ArrayList<>();
        List<Coach> coaches = coachRepository.findAll();

        for (Coach coach : coaches) {
            // Each coach has 64 seats (8 rows x 8 seats per row)
            for (int row = 1; row <= 8; row++) {
                for (char seat = 'A'; seat <= 'H'; seat++) {
                    String seatNumber = row + String.valueOf(seat);
                    String seatId = coach.getCoachId() + "_" + seatNumber;
                    seats.add(new Seat(seatId, coach, seatNumber));
                }
            }
        }

        seatRepository.saveAll(seats);
        System.out.println("Created " + seats.size() + " seats");
    }

    private void createBookings() {
        List<User> users = userRepository.findAll().stream()
                .filter(u -> "USER".equals(u.getRole()))
                .toList();

        if (users.isEmpty()) {
            System.out.println("No regular users found. Skipping booking creation.");
            return;
        }

        // Create sample bookings
        createSampleBooking("BK001", users.get(0), "SE1", "ST001", "ST014", "2025-01-20", "SE1_C1_1A", "Nguyen Van An");
        createSampleBooking("BK002", users.get(1), "SE2", "ST014", "ST001", "2025-01-21", "SE2_C2_2B", "Tran Thi Binh");
        createSampleBooking("BK003", users.get(2), "SE3", "ST001", "ST008", "2025-01-22", "SE3_C1_3C", "Le Van Cuong");
        createSampleBooking("BK004", users.get(3), "SE5", "ST001", "ST008", "2025-01-23", "SE5_C3_4D", "Pham Thi Dung");
        createSampleBooking("BK005", users.get(4), "SPT1", "ST014", "ST012", "2025-01-24", "SPT1_C1_5E", "Hoang Van Em");

        System.out.println("Created 5 sample bookings");
    }

    private void createSampleBooking(String bookingId, User user, String trainId, 
                                      String startStationId, String endStationId, 
                                      String travelDateStr, String seatId, String passengerName) {
        try {
            Seat seat = seatRepository.findById(seatId).orElseThrow();
            Station startStation = stationRepository.findById(startStationId).orElseThrow();
            Station endStation = stationRepository.findById(endStationId).orElseThrow();
            Date travelDate = Date.valueOf(travelDateStr);

            Booking booking = new Booking(
                bookingId,
                user,
                new Date(System.currentTimeMillis()),
                1,
                850000L,
                "CONFIRMED"
            );
            bookingRepository.save(booking);

            Ticket ticket = new Ticket(
                booking,
                seat,
                travelDate,
                startStation,
                endStation,
                passengerName
            );
            ticketRepository.save(ticket);
        } catch (Exception e) {
            System.err.println("Failed to create booking " + bookingId + ": " + e.getMessage());
        }
    }
}
