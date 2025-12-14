package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.bean.Admin;
import com.example.demo.bean.Doctor;
import com.example.demo.bean.Insurance;
import com.example.demo.bean.User;
import com.example.demo.dao.AdminRepository;
import com.example.demo.dao.DoctorRepository;
import com.example.demo.dao.UserRepository;

@Service
public class AdminService {

    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private UserRepository userRepository;

    // ------------------- ADMIN AUTH -------------------

    public boolean validateAdmin(String username, String password) {
        Admin admin = adminRepository.findByUsername(username);
        if (admin != null) {
            // Compare passwords (plain text for now, ideally use BCrypt)
            return admin.getPassword().equals(password); 
        }
        return false;
    }

    // ------------------- DOCTOR CRUD -------------------

    public Doctor addDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Doctor getDoctorById(Long doctorId) {
        return doctorRepository.findById(doctorId).orElse(null);
    }

    public Doctor editDoctor(Long doctorId, Doctor updatedDoctor) {
        Doctor existingDoctor = doctorRepository.findById(doctorId).orElse(null);
        if (existingDoctor != null) {
            existingDoctor.setUsername(updatedDoctor.getUsername());
            existingDoctor.setSpecialization(updatedDoctor.getSpecialization());
            existingDoctor.setEmail(updatedDoctor.getEmail());
            existingDoctor.setPhone(updatedDoctor.getPhone());
            existingDoctor.setStatus(updatedDoctor.getStatus());
            
            // Update password only if provided
            if(updatedDoctor.getPassword() != null && !updatedDoctor.getPassword().isEmpty()){
                existingDoctor.setPassword(updatedDoctor.getPassword());
            }

            return doctorRepository.save(existingDoctor);
        }
        return null;
    }

    public boolean deleteDoctor(Long doctorId) {
        if (doctorRepository.existsById(doctorId)) {
            doctorRepository.deleteById(doctorId);
            return true;
        }
        return false;
    }

    public Iterable<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // ------------------- USER CRUD -------------------

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User addUser(User user) {
        // IMPORTANT: Bidirectional Relationship Maintenance
        // If the incoming user has insurance details, we must tell the Insurance object
        // who it belongs to before saving.
        if (user.getInsurance() != null) {
            user.getInsurance().setUser(user);
        }
        
        // Ideally, encrypt the password here before saving
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User editUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id).orElse(null);
        
        if (existingUser != null) {
            // 1. Update Basic User Details
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setRole(updatedUser.getRole());
            existingUser.setAge(updatedUser.getAge());
            existingUser.setAddress(updatedUser.getAddress());
            existingUser.setGender(updatedUser.getGender());
            existingUser.setPhone(updatedUser.getPhone());
            
            // 2. Update Password (only if a new one is provided)
            if(updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()){
                existingUser.setPassword(updatedUser.getPassword());
            }
            
            // 3. Update One-to-One Insurance Relationship
            Insurance incomingInsurance = updatedUser.getInsurance();
            
            if (incomingInsurance != null) {
                Insurance existingInsurance = existingUser.getInsurance();
                
                if (existingInsurance != null) {
                    // Scenario A: User already has insurance. Update the existing record.
                    existingInsurance.setInsuranceNumber(incomingInsurance.getInsuranceNumber());
                    existingInsurance.setProviderName(incomingInsurance.getProviderName());
                    existingInsurance.setCoverageType(incomingInsurance.getCoverageType());
                    existingInsurance.setValidityStart(incomingInsurance.getValidityStart());
                    existingInsurance.setValidityEnd(incomingInsurance.getValidityEnd());
                    existingInsurance.setPremiumAmount(incomingInsurance.getPremiumAmount());
                    existingInsurance.setStatus(incomingInsurance.isStatus());
                } else {
                    // Scenario B: User had no insurance. Set the new one.
                    // Must link the back-reference
                    incomingInsurance.setUser(existingUser);
                    existingUser.setInsurance(incomingInsurance);
                }
            }
            
            return userRepository.save(existingUser);
        }
        return null;
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}