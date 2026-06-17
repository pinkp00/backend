package azh.bkd.demo.controller;

import azh.bkd.demo.model.*;
import azh.bkd.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public Map<String, Object> createBooking(@RequestBody Map<String, Object> request) {
        Long roomId = Long.valueOf(request.get("roomId").toString());
        Long userId = Long.valueOf(request.get("userId").toString());
        LocalDateTime start = LocalDateTime.parse(request.get("startTime").toString()); // Format: 2026-06-14T09:00:00
        LocalDateTime end = LocalDateTime.parse(request.get("endTime").toString());
        String type = request.get("type").toString(); // 'regular' ya 'reserved'

        Room room = roomRepository.findById(roomId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        // Conflict Check logic running
        List<Booking> conflicts = bookingRepository.findOverlappingBookings(roomId, start, end);
        
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setUser(user);
        booking.setStartTime(start);
        booking.setEndTime(end);

        Map<String, Object> response = new HashMap<>();

        if (!conflicts.isEmpty()) {
            // Conflict mil gaya! Booking automatic PENDING ho jayegi
            booking.setStatus("PENDING");
            booking.setConflictDesc("Conflict: Is time frame par pehle se hi class reserved/occupied hai.");
            response.put("message", "Conflict detected! Status set to PENDING.");
        } else {
            // Room free hai, book ho gaya!
            booking.setStatus("OCCUPIED_" + type.toUpperCase());
            response.put("message", "Room booked successfully!");
        }

        bookingRepository.save(booking);
        response.put("success", true);
        response.put("bookingStatus", booking.getStatus());
        
        return response;
    }

    @GetMapping("/all")
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}