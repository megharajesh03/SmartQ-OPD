package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.bean.User;
import com.example.demo.dao.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    // Method to validate user by username and password
    public User validateUser(String username, String password) {
        // Fetch user by username
        User user = userRepo.findByUsername(username);

        // Check if user exists and password matches
        if (user != null && user.getPassword().equals(password)) {
            return user;  // Return user if valid
        }

        // Return null if invalid
        return null;
    }
}
