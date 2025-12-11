package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.bean.Doctor;
import com.example.demo.dao.DoctorRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    // Validate doctor credentials (can be expanded with additional checks)
    public Doctor validateDoctor(String username, String password) {
        // Assuming the doctor's username is their email (or you can adjust to match your validation logic)
        Doctor doctor = doctorRepository.findByUsername(username);

        if (doctor != null && doctor.getPassword().equals(password)) {
            return doctor;
        }

        return null;  // Invalid login
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
            doctorRepository.save(doctor);  // Save the updated doctor status in the database
        }
    }

}
