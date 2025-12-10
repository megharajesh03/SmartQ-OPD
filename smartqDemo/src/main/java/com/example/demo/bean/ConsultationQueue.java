package com.example.demo.bean;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ConsultationQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // AUTO_INCREMENT behavior in SQL
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;  // Foreign Key to Doctor

    // Change the foreign key column name from 'id' to 'user_id' to avoid conflict
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Foreign Key to User

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsultationStatus consultationStatus;  // ENUM for consultation status

    private Timestamp scheduledTime;

    // Constructors, Getters, and Setters

    public ConsultationQueue() {
    }

    public ConsultationQueue(Doctor doctor, User user, ConsultationStatus consultationStatus, Timestamp scheduledTime) {
        this.doctor = doctor;
        this.user = user;
        this.consultationStatus = consultationStatus;
        this.scheduledTime = scheduledTime;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ConsultationStatus getConsultationStatus() {
        return consultationStatus;
    }

    public void setConsultationStatus(ConsultationStatus consultationStatus) {
        this.consultationStatus = consultationStatus;
    }

    public Timestamp getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Timestamp scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    @Override
    public String toString() {
        return "ConsultationQueue{id=" + id + ", doctor=" + doctor.getId() + ", patient=" + user.getId() + ", consultationStatus=" + consultationStatus + ", scheduledTime=" + scheduledTime + "}";
    }

    // Enum to represent the consultation status
    public enum ConsultationStatus {
        PENDING,
        IN_CONSULTATION,
        DONE
    }
}
