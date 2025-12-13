package com.example.demo.service;

import com.example.demo.bean.User;
import com.example.demo.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;
    

    // Add this method if it is missing
    public User getUserById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    // Validate user credentials based on role
    public User validateUser(String username, String password, String role) {
        User user = null;
        switch (role.toUpperCase()) {
//            case "ADMIN":
//                user = adminRepo.findByUsername(username);
//                break;
//            case "DOCTOR":
//                user = doctorRepo.findByUsername(username);
//                break;
            case "USER":
                user = userRepo.findByUsername(username);
                break;
        }

        if (user != null && user.getPassword().equals(password)) {
            return user;  // Valid user
        }
        return null;  // Invalid user
    }
    
    public User updateUser(User updatedUser) {
        // 1. Find existing user
        User existingUser = userRepo.findById(updatedUser.getId()).orElse(null);
        
        if (existingUser != null) {
            // 2. Update fields
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setAge(updatedUser.getAge());
            existingUser.setGender(updatedUser.getGender());
            existingUser.setPhone(updatedUser.getPhone());
            existingUser.setAddress(updatedUser.getAddress());
            existingUser.setInsuranceId(updatedUser.getInsuranceId());
            
            // Only update password if user typed something new
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(updatedUser.getPassword());
            }

            // 3. Save
            return userRepo.save(existingUser);
        }
        return null;
    }
    
}
