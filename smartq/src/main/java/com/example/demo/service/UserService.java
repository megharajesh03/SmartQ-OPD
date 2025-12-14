package com.example.demo.service;

import com.example.demo.bean.Insurance;
import com.example.demo.bean.User;
import com.example.demo.dao.UserRepository;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    // Get user by ID
    public User getUserById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    // Validate user credentials
    public User validateUser(String username, String password, String role) {
        User user = null;
        
        // Check if the role matches strictly
        if ("USER".equalsIgnoreCase(role)) {
            user = userRepo.findByUsername(username);
        }

        // If user exists and password matches
        if (user != null && user.getPassword().equals(password)) {
            return user; 
        }
        
        return null; // Invalid credentials or role
    }
    
 // Inside UserService.java

    public User updateUser(User updatedUser) {
        // 1. Find existing user
        User existingUser = userRepo.findById(updatedUser.getId()).orElse(null);
        
        if (existingUser != null) {
            
            // --- FIX START: Only update User fields if they are present in the request ---
            
            if (updatedUser.getUsername() != null && !updatedUser.getUsername().isEmpty()) {
                existingUser.setUsername(updatedUser.getUsername());
            }

            if (updatedUser.getRole() != null) {
                existingUser.setRole(updatedUser.getRole());
            }

            // check != 0 because 'int' defaults to 0 if not sent
            if (updatedUser.getAge() != 0) {
                existingUser.setAge(updatedUser.getAge());
            }

            if (updatedUser.getAddress() != null) {
                existingUser.setAddress(updatedUser.getAddress());
            }

            if (updatedUser.getGender() != null) {
                existingUser.setGender(updatedUser.getGender());
            }

            if (updatedUser.getPhone() != null) {
                existingUser.setPhone(updatedUser.getPhone());
            }

            // Only update password if user typed something new
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(updatedUser.getPassword());
            }
            
            // --- FIX END ---

            // 4. Handle Insurance One-to-One Relationship
            com.example.demo.bean.Insurance incomingInsurance = updatedUser.getInsurance();

            if (incomingInsurance != null) {
                com.example.demo.bean.Insurance existingInsurance = existingUser.getInsurance();

                if (existingInsurance != null) {
                    // Update existing insurance details
                    existingInsurance.setInsuranceNumber(incomingInsurance.getInsuranceNumber());
                    existingInsurance.setProviderName(incomingInsurance.getProviderName());
                    existingInsurance.setCoverageType(incomingInsurance.getCoverageType());
                    existingInsurance.setValidityStart(incomingInsurance.getValidityStart());
                    existingInsurance.setValidityEnd(incomingInsurance.getValidityEnd());
                    existingInsurance.setPremiumAmount(incomingInsurance.getPremiumAmount());
                    existingInsurance.setStatus(incomingInsurance.isStatus());
                } else {
                    // Create new insurance
                    incomingInsurance.setUser(existingUser);
                    existingUser.setInsurance(incomingInsurance);
                }
            }

            // 5. Save the updated entity
            return userRepo.save(existingUser);
        }
        return null;
    }

    public String checkInsuranceStatus(Long userId) {
        User user = userRepo.findById(userId).orElse(null);
        
        if (user == null) {
            return "USER_NOT_FOUND";
        }

        Insurance insurance = user.getInsurance();

        // 1. Check if Insurance exists
        if (insurance == null) {
            return "MISSING"; // Redirect to Insurance Page
        }

        // 2. Check if Insurance is Expired
        // We use new Date() to get the current time
        Date currentDate = new Date();
        if (insurance.getValidityEnd() != null && insurance.getValidityEnd().before(currentDate)) {
            return "EXPIRED"; // Redirect to Insurance Page
        }

        // 3. If present and not expired
        return "VALID"; // Redirect to User Home
    }
}