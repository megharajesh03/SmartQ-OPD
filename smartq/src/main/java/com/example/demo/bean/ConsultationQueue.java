package com.example.demo.bean;

import jakarta.persistence.*; // Or javax.persistence depending on version
import java.util.Date;

@Entity
public class ConsultationQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // New field: The simple number like 1, 2, 3 specific to the doctor
    private int tokenNumber; 

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Temporal(TemporalType.TIMESTAMP)
    private Date scheduledTime = new Date();

    @Enumerated(EnumType.STRING)
    private ConsultationStatus consultationStatus = ConsultationStatus.WAITING;

    // Added COMPLETED for billing logic
    public enum ConsultationStatus {
        WAITING,
        IN_CONSULTATION,
        COMPLETED 
    }

    // Getters and Setters for tokenNumber and others...
    public int getTokenNumber() { return tokenNumber; }
    public void setTokenNumber(int tokenNumber) { this.tokenNumber = tokenNumber; }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }
    public Date getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(Date scheduledTime) { this.scheduledTime = scheduledTime; }
    public ConsultationStatus getConsultationStatus() { return consultationStatus; }
    public void setConsultationStatus(ConsultationStatus consultationStatus) { this.consultationStatus = consultationStatus; }
}