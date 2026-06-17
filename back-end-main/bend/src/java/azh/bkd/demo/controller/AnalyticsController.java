package azh.bkd.demo.controller;

import azh.bkd.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/dashboard-stats")
    public Map<String, Object> getDashboardStats() {
        long totalUsers = userRepository.count();
        long totalRooms = roomRepository.count();
        
        // Filter out totals based on database entries
        long totalConflicts = bookingRepository.findAll().stream()
                .filter(b -> "PENDING".equals(b.getStatus())).count();
                
        long activeBookings = bookingRepository.findAll().stream()
                .filter(b -> b.getStatus() != null && b.getStatus().startsWith("OCCUPIED_")).count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("totalRooms", totalRooms);
        stats.put("totalConflicts", totalConflicts);
        stats.put("activeBookings", activeBookings);

        return stats;
    }
}