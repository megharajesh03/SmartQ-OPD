package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.bean.Doctor;
import com.example.demo.dao.DoctorRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    // --- NEW: In-Memory Storage for Served Count ---
    // This lives in RAM only. It clears automatically when the app restarts.
    private Map<Long, Integer> sessionServedCounts = new ConcurrentHashMap<>();


    // Validate doctor credentials
    public Doctor validateDoctor(String username, String password) {
        Doctor doctor = doctorRepository.findByUsername(username);
        if (doctor != null && doctor.getPassword().equals(password)) {
            return doctor;
        }
        return null;
    }

    // Get doctor by ID
    public Doctor getDoctorById(Long doctorId) {
        return doctorRepository.findById(doctorId).orElse(null);
    }

    // Update doctor status (Available or Not Available)
    public void setDoctorStatus(Long doctorId, Doctor.Status status) {
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if (doctor != null) {
            doctor.setStatus(status);
            doctorRepository.save(doctor);
        }
    }

    public Doctor updateDoctor(Doctor updatedInfo) {
        Doctor existingDoctor = doctorRepository.findById(updatedInfo.getId()).orElse(null);

        if (existingDoctor != null) {
            existingDoctor.setUsername(updatedInfo.getUsername());
            existingDoctor.setSpecialization(updatedInfo.getSpecialization());
            existingDoctor.setPassword(updatedInfo.getPassword());
            existingDoctor.setEmail(updatedInfo.getEmail());
            existingDoctor.setPhone(updatedInfo.getPhone());

            return doctorRepository.save(existingDoctor);
        }
        
        System.out.println("Error: Doctor ID not found: " + updatedInfo.getId());
        return null;
    }

    // --- NEW METHODS FOR SESSION COUNTING ---

    // 1. Get the current count from memory (RAM)
    public int getSessionServedCount(Long doctorId) {
        return sessionServedCounts.getOrDefault(doctorId, 0);
    }

    // 2. Increment the count in memory
    public void incrementSessionServedCount(Long doctorId) {
        // This adds 1 to the existing count safely
        sessionServedCounts.merge(doctorId, 1, Integer::sum);
    }
}