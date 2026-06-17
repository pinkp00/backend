package azh.bkd.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    private String status; // AVAILABLE, OCCUPIED_REGULAR, OCCUPIED_RESERVED, PENDING
    private String conflictDesc; // Conflict ki details save karne ke liye
}