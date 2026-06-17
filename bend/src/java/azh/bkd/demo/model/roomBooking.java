package com.example.azhly.model;

import jakarta.persistence.*;

@Entity
@Table(name = "room_bookings")
public class RoomBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String studentName;
    private String roomNo;      // e.g., "Lab-3"
    private String day;         // e.g., "MON"
    private String timeSlot;    // e.g., "07:00 - 08:30"
    private String status;      // "PENDING", "APPROVED", "REJECTED"
    private String adminMessage; // Message back to student

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getRoomNo() { return roomNo; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }
    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }
    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getAdminMessage() { return adminMessage; }
    public void setAdminMessage(String adminMessage) { this.adminMessage = adminMessage; }
}