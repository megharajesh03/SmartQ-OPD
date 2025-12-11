package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.bean.Admin;
import com.example.demo.bean.Doctor;
import com.example.demo.dao.AdminRepository;
import com.example.demo.dao.DoctorRepository;

@Service
public class AdminService {

    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private AdminRepository adminRepository;
    
    
    public boolean validateAdmin(String username, String password) {
        // Fetch admin from the database by username
        Admin admin = adminRepository.findByUsername(username);

        if (admin != null) {
            // Compare the provided password with the stored password (hash comparison if using bcrypt)
            return admin.getPassword().equals(password);  // Change this to bcrypt comparison if using encryption
        }

        return false;  // Return false if admin not found or password doesn't match
    }

    // Add new doctor
    public Doctor addDoctor(Doctor doctor) {
    	
        return doctorRepository.save(doctor);
        
    }

    // Get doctor by ID
    public Doctor getDoctorById(Long doctorId) {
        return doctorRepository.findById(doctorId).orElse(null);
    }

    // Edit doctor details
    public Doctor editDoctor(Long doctorId, Doctor updatedDoctor) {
        Doctor existingDoctor = doctorRepository.findById(doctorId).orElse(null);
        if (existingDoctor != null) {
            existingDoctor.setUsername(updatedDoctor.getUsername());
            existingDoctor.setSpecialization(updatedDoctor.getSpecialization());
            existingDoctor.setEmail(updatedDoctor.getEmail());
            existingDoctor.setPhone(updatedDoctor.getPhone());
            existingDoctor.setPassword(updatedDoctor.getPassword());
            existingDoctor.setStatus(updatedDoctor.getStatus());
            return doctorRepository.save(existingDoctor);
        }
        return null;  // Return null if doctor doesn't exist
    }

    // Delete doctor
    public boolean deleteDoctor(Long doctorId) {
        if (doctorRepository.existsById(doctorId)) {
            doctorRepository.deleteById(doctorId);
            return true;
        }
        return false;
    }

    // View all doctors
    public Iterable<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
}
