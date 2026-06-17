package com.example.azhly.repository;
import com.example.azhly.model.RoomBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomBookingRepository extends JpaRepository<RoomBooking, Long> {
    
    // Custom query to find existing approved bookings for conflict check
    List<RoomBooking> findByRoomNoAndDayAndTimeSlotAndStatus(
        String roomNo, String day, String timeSlot, String status
    );
    
    List<RoomBooking> findByStudentName(String studentName);
}