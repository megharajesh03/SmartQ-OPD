package com.example.demo.service;

import com.example.demo.bean.User;
import com.example.demo.dao.UserRepository;
import com.example.demo.dao.AdminRepository;
import com.example.demo.dao.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private AdminRepository adminRepo;
    @Autowired
    private DoctorRepository doctorRepo;

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
    
}
