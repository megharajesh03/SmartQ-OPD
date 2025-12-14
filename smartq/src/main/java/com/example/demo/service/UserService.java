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
    
    // Update User Profile
    public User updateUser(User updatedUser) {
        // 1. Find existing user
        User existingUser = userRepo.findById(updatedUser.getId()).orElse(null);
        
        if (existingUser != null) {
            // 2. Update basic fields
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setAge(updatedUser.getAge());
            existingUser.setGender(updatedUser.getGender());
            existingUser.setPhone(updatedUser.getPhone());
            existingUser.setAddress(updatedUser.getAddress());
            
            // 3. Only update password if user typed something new
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(updatedUser.getPassword());
            }

            // 4. Handle Insurance One-to-One Relationship
            // We removed setInsuranceId. We must update the Insurance Object.
            Insurance incomingInsurance = updatedUser.getInsurance();

            if (incomingInsurance != null) {
                Insurance existingInsurance = existingUser.getInsurance();

                if (existingInsurance != null) {
                    // Scenario A: Update existing insurance details
                    // We update fields individually to keep the same Database ID
                    existingInsurance.setInsuranceNumber(incomingInsurance.getInsuranceNumber());
                    existingInsurance.setProviderName(incomingInsurance.getProviderName());
                    existingInsurance.setCoverageType(incomingInsurance.getCoverageType());
                    existingInsurance.setValidityStart(incomingInsurance.getValidityStart());
                    existingInsurance.setValidityEnd(incomingInsurance.getValidityEnd());
                    existingInsurance.setPremiumAmount(incomingInsurance.getPremiumAmount());
                    existingInsurance.setStatus(incomingInsurance.isStatus());
                } else {
                    // Scenario B: User had no insurance, link the new one
                    // Crucial: Set the user back-reference so the Foreign Key is saved
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