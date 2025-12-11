package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.bean.Doctor;
import com.example.demo.dao.DoctorRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

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
        // 1. Find the existing doctor record by ID
        // We use the ID coming from the hidden input in the form
        Doctor existingDoctor = doctorRepository.findById(updatedInfo.getId()).orElse(null);

        if (existingDoctor != null) {
            // 2. Update ALL editable fields
            existingDoctor.setUsername(updatedInfo.getUsername());
            existingDoctor.setSpecialization(updatedInfo.getSpecialization());
            existingDoctor.setPassword(updatedInfo.getPassword());
            
            // --- THESE WERE MISSING IN YOUR CODE ---
            existingDoctor.setEmail(updatedInfo.getEmail());
            existingDoctor.setPhone(updatedInfo.getPhone());
            // ---------------------------------------

            // 3. Save the changes
            return doctorRepository.save(existingDoctor);
        }
        
        System.out.println("Error: Doctor ID not found: " + updatedInfo.getId());
        return null;
    }
}