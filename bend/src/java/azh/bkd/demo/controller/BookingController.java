package com.example.azhly.controller;

import com.example.azhly.model.RoomBooking;
import com.example.azhly.repository.RoomBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*") // Simple cross origin for testing
public class BookingController {

    @Autowired
    private RoomBookingRepository bookingRepository;

    // 1. Student sends a booking request
    @PostMapping("/request")
    public ResponseEntity<?> requestRoom(@RequestBody RoomBooking booking) {
        booking.setStatus("PENDING");
        booking.setAdminMessage("Awaiting Admin Review");
        RoomBooking saved = bookingRepository.save(booking);
        return ResponseEntity.ok(saved);
    }

    // 2. Admin Reviews: Accepts or Rejects WITH CONFLICT CHECKING
    @PostMapping("/review/{id}")
    public ResponseEntity<?> reviewBooking(
            @PathVariable Long id, 
            @RequestParam String action, // "APPROVE" or "REJECT"
            @RequestParam(required = false, defaultValue = "") String message
    ) {
        RoomBooking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if ("APPROVE".equalsIgnoreCase(action)) {
            // CRITICAL TESTING LOGIC: Check for Room Conflicts!
            List<RoomBooking> conflicts = bookingRepository.findByRoomNoAndDayAndTimeSlotAndStatus(
                    booking.getRoomNo(), booking.getDay(), booking.getTimeSlot(), "APPROVED"
            );

            if (!conflicts.isEmpty()) {
                // Conflict Found! Auto Reject and inform Admin/Student
                booking.setStatus("REJECTED");
                booking.setAdminMessage("Automated System: Slot Conflict! Room already booked by " 
                                        + conflicts.get(0).getStudentName());
                bookingRepository.save(booking);
                return ResponseEntity.badRequest().body("Conflict Detected! This room is already booked for this slot.");
            }

            // No Conflict -> Approve
            booking.setStatus("APPROVED");
            booking.setAdminMessage(message.isEmpty() ? "Approved by Admin. Room is yours!" : message);
        } else {
            // Admin Rejected manually
            booking.setStatus("REJECTED");
            booking.setAdminMessage(message.isEmpty() ? "Rejected by Admin due to schedule adjustments." : message);
        }

        RoomBooking updated = bookingRepository.save(booking);
        return ResponseEntity.ok(updated);
    }

    // 3. Get all bookings for Admin panel
    @GetMapping("/all")
    public List<RoomBooking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // 4. Get specific student updates
    @GetMapping("/student/{name}")
    public List<RoomBooking> getStudentBookings(@PathVariable String name) {
        return bookingRepository.findByStudentName(name);
    }
}